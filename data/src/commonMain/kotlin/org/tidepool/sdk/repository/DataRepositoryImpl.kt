package org.tidepool.sdk.repository

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.di.provideDataApi
import org.tidepool.sdk.database.BasalAutomatedDataDao
import org.tidepool.sdk.database.BolusDataDao
import org.tidepool.sdk.database.CgmSettingsDataDao
import org.tidepool.sdk.database.ControllerSettingsDataDao
import org.tidepool.sdk.database.ContinuousGlucoseDataDao
import org.tidepool.sdk.database.DeviceEventDataDao
import org.tidepool.sdk.database.DosingDecisionDataDao
import org.tidepool.sdk.database.FoodDataDao
import org.tidepool.sdk.database.InsulinDataDao
import org.tidepool.sdk.database.PumpSettingsDataDao
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity
import org.tidepool.sdk.database.entity.data.BolusDataEntity
import org.tidepool.sdk.database.entity.data.CgmSettingsDataEntity
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity
import org.tidepool.sdk.database.entity.data.ControllerSettingsDataEntity
import org.tidepool.sdk.database.entity.data.DeviceEventDataEntity
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import org.tidepool.sdk.database.entity.data.InsulinDataEntity
import org.tidepool.sdk.database.entity.data.PumpSettingsDataEntity
import org.tidepool.sdk.database.entity.data.toEntity
import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.CgmSettingsDataDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.dto.data.ControllerSettingsDataDto
import org.tidepool.sdk.dto.data.DeviceEventDataDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.FoodDataDto
import org.tidepool.sdk.dto.data.InsulinDataDto
import org.tidepool.sdk.dto.data.PumpSettingsDataDto
import org.tidepool.sdk.dto.data.toDomain
import org.tidepool.sdk.dto.data.toDto
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.runCatchingNetworkExceptions
import kotlinx.datetime.Instant
import kotlin.collections.toTypedArray
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tidepool.sdk.database.entity.data.toDto

class DataRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
    private val basalAutomatedDataDao: BasalAutomatedDataDao,
    private val bolusDataDao: BolusDataDao,
    private val continuousGlucoseDataDao: ContinuousGlucoseDataDao,
    private val dosingDecisionDataDao: DosingDecisionDataDao,
    private val foodDataDao: FoodDataDao,
    private val insulinDataDao: InsulinDataDao,
    private val deviceEventDataDao: DeviceEventDataDao,
    private val cgmSettingsDataDao: CgmSettingsDataDao,
    private val controllerSettingsDataDao: ControllerSettingsDataDao,
    private val pumpSettingsDataDao: PumpSettingsDataDao,
    private val keyValueStorage: KeyValueStorage,
) : DataRepository {

    private val KEY_CACHED_DATA_SET_ID: String = "KEY_CACHED_DATA_SET_ID"

    private val dataApi: DataApi
        get() = provideDataApi(environmentRepository.getKtorfit(httpClient))

    private var cachedDataSetId: String? = null
        get() = field ?: keyValueStorage.getString(KEY_CACHED_DATA_SET_ID)
            .also { field = it }
        set(value) {
            field = value
            keyValueStorage.putString(KEY_CACHED_DATA_SET_ID, value)
        }

    private val dataSetIdMutex: Mutex = Mutex()
    private var dataSetIdDeferred: CompletableDeferred<String>? = null

    override suspend fun awaitOrCreateCachedDataSetId(
        create: suspend () -> Result<String>,
    ): Result<String> {
        // Fast-path when already available
        cachedDataSetId?.let { return Result.success(it) }

        var isCreator = false
        val localDeferred: CompletableDeferred<String> = dataSetIdMutex.withLock {
            // Re-check after acquiring the lock
            cachedDataSetId?.let { return Result.success(it) }
            if (dataSetIdDeferred == null) {
                dataSetIdDeferred = CompletableDeferred()
                isCreator = true
            }
            dataSetIdDeferred!!
        }

        if (isCreator) {
            val result = create()
            dataSetIdMutex.withLock {
                if (result.isSuccess) {
                    val id = result.getOrThrow()
                    cachedDataSetId = id
                    dataSetIdDeferred?.complete(id)
                } else {
                    val throwable = result.exceptionOrNull()
                        ?: IllegalStateException("Unknown error creating dataset id")
                    dataSetIdDeferred?.completeExceptionally(throwable)
                }
                dataSetIdDeferred = null
            }
            return result
        }

        return try {
            Result.success(localDeferred.await())
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getDataForUser(
        userId: String,
        uploadId: String?,
        deviceId: String?,
        types: List<DataType>?,
        startDate: Instant?,
        endDate: Instant?,
        latest: Boolean?,
        dexcom: Boolean?,
        carelink: Boolean?,
        medtronic: Boolean?,
        sessionToken: String,
    ): Result<List<BaseData>> = runCatchingNetworkExceptions {
        val typesParam = types
            ?.map { it.toDto() }
            ?.toTypedArray()
            ?.let { DataApi.CommaSeparatedArray(*it) }

        dataApi.getDataForUser(
            sessionToken = sessionToken,
            userId = userId,
            uploadId = uploadId,
            deviceId = deviceId,
            types = typesParam,
            startDate = startDate.toString(),
            endDate = endDate.toString(),
            latest = latest,
            dexcom = dexcom,
            carelink = carelink,
            medtronic = medtronic
        )
    }.mapList { it.toDomain() }

    // Data Sets operations
    override suspend fun getUserDataSets(
        userId: String,
        sessionToken: String,
        page: Int?,
        size: Int?,
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSets(sessionToken, userId)
    }.mapList { it.toDomain() }

    override suspend fun createDataSet(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSet(
            sessionToken = sessionToken,
            userId = userId,
            newDataSet = newDataSet.toDto(),
        )
    }.map { it.toDomain() }

    override suspend fun getDataSet(
        dataSetId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSet(sessionToken, dataSetId)
    }.map { it.toDomain() }

    override suspend fun updateDataSet(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSet(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            dataSet = dataSet.toDto(),
        )
    }.map { it.toDomain() }

    override suspend fun deleteDataSet(
        dataSetId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSet(sessionToken, dataSetId)
    }

    override suspend fun uploadDataToDataSet(
        data: List<BaseData>,
        sessionToken: String,
    ): Result<List<BaseData>> {
        val dtos = data.map { it.toDto() }
        if (data.isNotEmpty() && dtos.isEmpty()) {
            return Result.failure(Throwable("Mapping data to DTO failed"))
        }
        dtos.cache()
        return Result.success(data)
    }

    override suspend fun deleteDataSetData(
        dataSetId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSetData(sessionToken, dataSetId)
    }

    // Legacy datasets operations
    override suspend fun getUserDataSetsLegacy(
        userId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSetsLegacy(sessionToken, userId)
    }.mapList { it.toDomain() }

    override suspend fun createDataSetLegacy(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSetLegacy(
            sessionToken = sessionToken,
            userId = userId,
            newDataSet = newDataSet.toDto(),
        )
    }.map { it.toDomain() }

    override suspend fun getDataSetLegacy(
        dataSetId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSetLegacy(sessionToken, dataSetId)
    }.map { it.toDomain() }

    override suspend fun updateDataSetLegacy(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSetLegacy(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            dataSet = dataSet.toDto(),
        )
    }.map { it.toDomain() }

    override suspend fun deleteDataSetLegacy(
        dataSetId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSetLegacy(sessionToken, dataSetId)
    }

    override suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseData>,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.uploadDataToDataSetLegacy(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            data = data.map { it.toDto() }
        )
    }.mapList { it.toDomain() }

    // Data Sources operations
    override suspend fun getUserDataSources(
        userId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSources(sessionToken, userId)
    }.mapList { it.toDomain() }

    override suspend fun createDataSource(
        userId: String,
        newDataSource: NewDataSource,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSource(
            sessionToken = sessionToken,
            userId = userId,
            newDataSource = newDataSource.toDto()
        )
    }.map { it.toDomain() }

    override suspend fun deleteAllDataSources(
        userId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.deleteAllDataSources(sessionToken, userId)
    }

    override suspend fun getDataSource(
        dataSourceId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSource(sessionToken, dataSourceId)
    }.map { it.toDomain() }

    override suspend fun updateDataSource(
        dataSourceId: String,
        dataSource: DataSource,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSource(
            sessionToken = sessionToken,
            dataSourceId = dataSourceId,
            dataSource = dataSource.toDto()
        )
    }.map { it.toDomain() }

    override suspend fun deleteDataSource(
        dataSourceId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSource(sessionToken, dataSourceId)
    }

    // Additional data operations
    override suspend fun deleteAllUserData(
        userId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        dataApi.deleteAllUserData(sessionToken, userId)
    }

    override suspend fun uploadCachedData(
        userId: String,
        sessionToken: String,
        dataSetId: String,
    ): Result<Unit> = listOf(
        basalAutomatedDataDao.getAll(),
        bolusDataDao.getAll(),
        continuousGlucoseDataDao.getAll(),
        dosingDecisionDataDao.getAll(),
        foodDataDao.getAll(),
        insulinDataDao.getAll(),
    ).flatten().let { entities ->
        Logger.v(javaClass.simpleName) { "Uploading ${entities.size} entities to data set $dataSetId" }
        runCatchingNetworkExceptions {
            dataApi.uploadDataToDataSet(
                sessionToken = sessionToken,
                dataSetId = dataSetId,
                data = entities.map { it.toDto() },
            )
        }.map {
            entities.forEach { entity ->
                when (entity) {
                    is BasalAutomatedDataEntity -> basalAutomatedDataDao.delete(entity)
                    is BolusDataEntity -> bolusDataDao.delete(entity)
                    is ContinuousGlucoseDataEntity -> continuousGlucoseDataDao.delete(entity)
                    is DosingDecisionDataEntity -> dosingDecisionDataDao.delete(entity)
                    is FoodDataEntity -> foodDataDao.delete(entity)
                    is InsulinDataEntity -> insulinDataDao.delete(entity)
                    is DeviceEventDataEntity -> deviceEventDataDao.delete(entity)
                    is CgmSettingsDataEntity -> cgmSettingsDataDao.delete(entity)
                    is ControllerSettingsDataEntity -> controllerSettingsDataDao.delete(entity)
                    is PumpSettingsDataEntity -> pumpSettingsDataDao.delete(entity)
                }
            }
        }
    }

    override fun clearCachedDataSetId() {
        cachedDataSetId = null
    }

    private suspend fun Result<List<BaseDataDto>>.cacheOnFailure(
        toUpload: List<BaseDataDto>,
    ) = fold(
        onSuccess = { Result.success(it) },
        onFailure = { ex ->
            toUpload.cache()
            Result.failure(ex)
        },
    )

    private suspend fun List<BaseDataDto>.cache() = forEach { dto ->
        when (dto) {
            is BasalAutomatedDataDto -> basalAutomatedDataDao.insert(dto.toEntity())
            is BolusDataDto -> bolusDataDao.insert(dto.toEntity())
            is ContinuousGlucoseDataDto -> continuousGlucoseDataDao.insert(dto.toEntity())
            is DosingDecisionDataDto -> dosingDecisionDataDao.insert(dto.toEntity())
            is FoodDataDto -> foodDataDao.insert(dto.toEntity())
            is InsulinDataDto -> insulinDataDao.insert(dto.toEntity())
            is DeviceEventDataDto -> deviceEventDataDao.insert(dto.toEntity())
            is CgmSettingsDataDto -> cgmSettingsDataDao.insert(dto.toEntity())
            is ControllerSettingsDataDto -> controllerSettingsDataDao.insert(dto.toEntity())
            is PumpSettingsDataDto -> pumpSettingsDataDao.insert(dto.toEntity())
            else -> Logger.w(javaClass.simpleName) { "Unknown data type: ${dto::class.simpleName}" }
        }
    }
}