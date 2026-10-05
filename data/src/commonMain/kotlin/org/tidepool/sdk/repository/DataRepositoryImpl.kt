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
import org.tidepool.sdk.database.entity.data.BaseDataEntity
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
    ): Result<List<BaseData>> {
        Logger.d(javaClass.simpleName) {
            "uploadDataToDataSet(): ${data.map { it.javaClass.simpleName }}"
        }
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
        sessionToken: String,
        dataSetId: String,
    ): Result<Boolean> {
        // One request per data type, as iOS TidepoolService does: each request stays near iOS's
        // ~1 MB target, and a type the server rejects or that times out can't hold back the
        // others. It's retried on the next call, like iOS retries a failed type.
        suspend fun <T : BaseDataEntity> upload(
            type: String,
            limit: Int,
            getAll: suspend (Int) -> List<T>,
            delete: suspend (List<T>) -> Unit,
        ) = uploadBatch(sessionToken, dataSetId, type, limit, getAll, delete)

        val results = listOfNotNull(
            upload("basal", UPLOAD_BATCH_LIMIT, basalAutomatedDataDao::getAll, basalAutomatedDataDao::delete),
            upload("bolus", UPLOAD_BATCH_LIMIT, bolusDataDao::getAll, bolusDataDao::delete),
            upload("cbg", UPLOAD_BATCH_LIMIT, continuousGlucoseDataDao::getAll, continuousGlucoseDataDao::delete),
            upload(
                "dosingDecision",
                DOSING_DECISION_UPLOAD_BATCH_LIMIT,
                dosingDecisionDataDao::getAll,
                dosingDecisionDataDao::delete,
            ),
            upload("food", UPLOAD_BATCH_LIMIT, foodDataDao::getAll, foodDataDao::delete),
            upload("insulin", UPLOAD_BATCH_LIMIT, insulinDataDao::getAll, insulinDataDao::delete),
        )
        val (uploaded, failed) = results.partition { it.isSuccess }

        // Only a pass where nothing got through is a failure, so a caller still retries when the
        // network is down, but one stuck type doesn't stop the rest from draining.
        return if (uploaded.isEmpty() && failed.isNotEmpty()) {
            failed.first()
        } else {
            Result.success(uploaded.any { it.getOrThrow() })
        }
    }

    /**
     * Uploads up to [limit] of one table's oldest rows in their own request and deletes them once
     * the server accepts them. Returns null when the table is empty, otherwise whether the batch
     * came back full (more rows may be waiting behind it).
     */
    private suspend fun <T : BaseDataEntity> uploadBatch(
        sessionToken: String,
        dataSetId: String,
        type: String,
        limit: Int,
        getAll: suspend (Int) -> List<T>,
        delete: suspend (List<T>) -> Unit,
    ): Result<Boolean>? {
        val rows = getAll(limit)
        if (rows.isEmpty()) return null

        Logger.v(javaClass.simpleName) { "Uploading ${rows.size} $type entities" }
        return runCatchingNetworkExceptions {
            dataApi.uploadDataToDataSet(
                sessionToken = sessionToken,
                dataSetId = dataSetId,
                data = rows.map { it.toDto() },
            )
        }.map {
            // One list delete per table: Room runs it as a single transaction instead of one per
            // row, which matters with up to 1000 rows per batch.
            delete(rows)
            rows.size == limit
        }.onFailure {
            Logger.e(javaClass.simpleName, it) { "Uploading $type failed, it stays cached for the next attempt" }
        }
    }

    override fun clearCachedDataSetId() {
        cachedDataSetId = null
    }

    override suspend fun hasCachedData(): Boolean =
        basalAutomatedDataDao.getAll(1).isNotEmpty() ||
            bolusDataDao.getAll(1).isNotEmpty() ||
            continuousGlucoseDataDao.getAll(1).isNotEmpty() ||
            dosingDecisionDataDao.getAll(1).isNotEmpty() ||
            foodDataDao.getAll(1).isNotEmpty() ||
            insulinDataDao.getAll(1).isNotEmpty()

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

    companion object {
        // Per-request limits from iOS TidepoolService (glucoseDataLimit / doseDataLimit 1000,
        // dosingDecisionDataLimit 50, aiming at ~1 MB each). One difference: iOS caps doses at 1000
        // across all dose kinds, while basal, bolus and insulin each get their own request here.
        private const val UPLOAD_BATCH_LIMIT = 1000
        private const val DOSING_DECISION_UPLOAD_BATCH_LIMIT = 50
    }
}