package org.tidepool.sdk.service

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.tidepool.sdk.AppLifecycleProvider
import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.ClientSoftware
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.DeduplicatorDescriptor
import org.tidepool.sdk.model.data.DeviceTag
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.repository.UserRepository
import kotlin.time.Duration
import kotlinx.datetime.Instant
import java.util.Collections.emptyList
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

class DataService internal constructor(
    private val dataRepository: DataRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
) {

    private val TAG = javaClass.simpleName
    fun startLifecycleAwareRecurrentUpload(
        lifecycleProvider: AppLifecycleProvider,
        scope: CoroutineScope,
        period: Duration,
        delay: Duration,
    ) = LifecycleAwareDataUploadManager(
        lifecycleProvider = lifecycleProvider,
        scope = scope,
    ).apply {
        configure(
            period = period,
            delay = delay,
            action = {
                Logger.d("DataService") { "Uploading cached data" }
                tokenProvider.getToken().flatMap { sessionToken ->
                    Logger.v("DataService") { "Have token" }
                    getDataSetId().flatMap { dataSetId ->
                        Logger.i("DataService") { "Have data set id" }
                        dataRepository.uploadCachedData(
                            userId = userRepository.getCurrentUser(sessionToken)
                                .getOrThrow().userId,
                            sessionToken = sessionToken,
                            dataSetId = dataSetId,
                        )
                    }
                        .onSuccess { Logger.d(TAG) { "Cached data uploaded successfully" } }
                        .onFailure { Logger.e(TAG, it) { "Failed to upload cached data: " } }
                }
            },
        )
        start()
    }

    suspend fun getDataForUser(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<DataType> = emptyList(),
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        dexcom: Boolean? = null,
        carelink: Boolean? = null,
        medtronic: Boolean? = null,
    ): Result<List<BaseData>> = tokenProvider.getToken().flatMap {
        dataRepository.getDataForUser(
            userId = userId,
            uploadId = uploadId,
            deviceId = deviceId,
            types = types,
            startDate = startDate,
            endDate = endDate,
            latest = latest,
            dexcom = dexcom,
            carelink = carelink,
            medtronic = medtronic,
            sessionToken = it,
        )
    }

    // Data Sets operations
    suspend fun getUserDataSets(userId: String): Result<List<DataSet>> =
        tokenProvider.getToken().flatMap {
            dataRepository.getUserDataSets(
                userId = userId,
                sessionToken = it,
            )
        }

    suspend fun getUserDataSets(): Result<List<DataSet>> =
        tokenProvider.getToken().flatMap { token ->
            userRepository.getCurrentUser(token).flatMap { user ->
                dataRepository.getUserDataSets(
                    userId = user.userId,
                    sessionToken = token,
                )
            }
        }

    // Data Sets operations
    suspend fun getUserDataSetsPaginator(
        userId: String,
        pageSize: Int = 42,
        onPageLoadSuccess: suspend (List<DataSet>, Boolean) -> Unit,
        onPageLoadFailure: suspend (Throwable) -> Unit,
    ): Paginator<Int, List<DataSet>> = PaginatorImpl(
        initialKey = 0,
        onRequest = { page ->
            tokenProvider.getToken().flatMap {
                dataRepository.getUserDataSets(
                    userId = userId,
                    sessionToken = it,
                    page = page,
                    size = pageSize,
                )
            }
        },
        getNextKey = { page, offset -> offset + page.size },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize }
    )

    suspend fun createDataSet(userId: String, newDataSet: NewDataSet): Result<DataSet> =
        tokenProvider.getToken().flatMap {
            dataRepository.createDataSet(
                userId = userId,
                newDataSet = newDataSet,
                sessionToken = it,
            )
        }

    suspend fun createDataSet(newDataSet: NewDataSet): Result<DataSet> =
        tokenProvider.getToken().flatMap { token ->
            Logger.d(TAG) { "Creating data set with token: ${token.take(5)}..." }
            userRepository.getCurrentUser(token).flatMap { user ->
                Logger.d(TAG) { "Creating data set for user: ${user.userId}" }
                dataRepository.createDataSet(
                    userId = user.userId,
                    newDataSet = newDataSet,
                    sessionToken = token,
                )
            }
        }

    suspend fun getDataSet(dataSetId: String): Result<DataSet> =
        tokenProvider.getToken().flatMap {
            dataRepository.getDataSet(
                dataSetId = dataSetId,
                sessionToken = it,
            )
        }

    suspend fun updateDataSet(dataSetId: String, dataSet: DataSet): Result<DataSet> =
        tokenProvider.getToken().flatMap {
            dataRepository.updateDataSet(
                dataSetId = dataSetId,
                dataSet = dataSet,
                sessionToken = it,
            )
        }

    suspend fun deleteDataSet(dataSetId: String): Result<Unit> =
        tokenProvider.getToken().flatMap {
            dataRepository.deleteDataSet(
                dataSetId = dataSetId,
                sessionToken = it,
            )
        }

    suspend fun uploadDataToDataSet(
        data: List<BaseData>,
    ): Result<List<BaseData>> =
        tokenProvider.getToken().flatMap {
            dataRepository.uploadDataToDataSet(
                data = data,
                sessionToken = it,
            )
        }

    suspend fun deleteDataSetData(dataSetId: String): Result<Unit> =
        tokenProvider.getToken().flatMap {
            dataRepository.deleteDataSetData(
                dataSetId = dataSetId,
                sessionToken = it,
            )
        }

    // Legacy datasets operations
    @Deprecated("Use getUserDataSets instead", ReplaceWith("getUserDataSets"))
    suspend fun getUserDataSetsLegacy(userId: String): Result<List<DataSet>> =
        tokenProvider.getToken().flatMap {
            dataRepository.getUserDataSetsLegacy(
                userId = userId,
                sessionToken = it,
            )
        }

    @Deprecated("Use createDataSet instead", ReplaceWith("createDataSet"))
    suspend fun createDataSetLegacy(userId: String, newDataSet: NewDataSet): Result<DataSet> =
        tokenProvider.getToken().flatMap {
            dataRepository.createDataSetLegacy(
                userId = userId,
                newDataSet = newDataSet,
                sessionToken = it,
            )
        }

    @Deprecated("Use getDataSet instead", ReplaceWith("getDataSet"))
    suspend fun getDataSetLegacy(dataSetId: String): Result<DataSet> =
        tokenProvider.getToken().flatMap {
            dataRepository.getDataSetLegacy(
                dataSetId = dataSetId,
                sessionToken = it,
            )
        }

    @Deprecated("Use updateDataSet instead", ReplaceWith("updateDataSet"))
    suspend fun updateDataSetLegacy(dataSetId: String, dataSet: DataSet): Result<DataSet> =
        tokenProvider.getToken().flatMap {
            dataRepository.updateDataSetLegacy(
                dataSetId = dataSetId,
                dataSet = dataSet,
                sessionToken = it,
            )
        }

    @Deprecated("Use deleteDataSet instead", ReplaceWith("deleteDataSet"))
    suspend fun deleteDataSetLegacy(dataSetId: String): Result<Unit> =
        tokenProvider.getToken().flatMap {
            dataRepository.deleteDataSetLegacy(
                dataSetId = dataSetId,
                sessionToken = it,
            )
        }

    @Deprecated("Use uploadDataToDataSet instead", ReplaceWith("uploadDataToDataSet"))
    suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseData>,
    ): Result<List<BaseData>> =
        tokenProvider.getToken().flatMap {
            dataRepository.uploadDataToDataSetLegacy(
                dataSetId = dataSetId,
                data = data,
                sessionToken = it,
            )
        }

    // Data Sources operations
    suspend fun getUserDataSources(userId: String): Result<List<DataSource>> =
        tokenProvider.getToken().flatMap {
            dataRepository.getUserDataSources(
                userId = userId,
                sessionToken = it,
            )
        }

    suspend fun getUserDataSourcesPaginator(
        userId: String,
        pageSize: Int = 42,
        onPageLoadSuccess: suspend (List<DataSource>, Boolean) -> Unit,
        onPageLoadFailure: suspend (Throwable) -> Unit,
    ): Paginator<Int, List<DataSource>> = PaginatorImpl(
        initialKey = 0,
        onRequest = { page ->
            tokenProvider.getToken().flatMap {
                dataRepository.getUserDataSources(
                    userId = userId,
                    sessionToken = it,
                )
            }
        },
        getNextKey = { page, offset -> offset + page.size },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize },
    )

    suspend fun createDataSource(userId: String, newDataSource: NewDataSource): Result<DataSource> =
        tokenProvider.getToken().flatMap {
            dataRepository.createDataSource(
                userId = userId,
                newDataSource = newDataSource,
                sessionToken = it,
            )
        }

    suspend fun deleteAllDataSources(userId: String): Result<Unit> =
        tokenProvider.getToken().flatMap {
            dataRepository.deleteAllDataSources(
                userId = userId,
                sessionToken = it,
            )
        }

    suspend fun getDataSource(dataSourceId: String): Result<DataSource> =
        tokenProvider.getToken().flatMap {
            dataRepository.getDataSource(
                dataSourceId = dataSourceId,
                sessionToken = it,
            )
        }

    suspend fun updateDataSource(dataSourceId: String, dataSource: DataSource): Result<DataSource> =
        tokenProvider.getToken().flatMap {
            dataRepository.updateDataSource(
                dataSourceId = dataSourceId,
                dataSource = dataSource,
                sessionToken = it,
            )
        }

    suspend fun deleteDataSource(dataSourceId: String): Result<Unit> =
        tokenProvider.getToken().flatMap {
            dataRepository.deleteDataSource(
                dataSourceId = dataSourceId,
                sessionToken = it,
            )
        }

    // Additional data operations
    suspend fun deleteAllUserData(userId: String): Result<Unit> =
        tokenProvider.getToken().flatMap {
            dataRepository.deleteAllUserData(
                userId = userId,
                sessionToken = it,
            )
        }

    suspend fun uploadData(data: List<BaseData>): Result<List<BaseData>> = if (data.isEmpty()) {
        Result.failure(IllegalArgumentException("Data list is empty"))
    } else {
        tokenProvider.getToken().flatMap {
            uploadDataToDataSet(data = data)
                .onSuccess {
                    Logger.d(TAG) { "${data.map { it.javaClass.simpleName }} saved for upload" }
                    Logger.v(TAG) {
                        "${data.map { "${it.javaClass.simpleName}: ${it.annotations}" }} saved for upload"
                    }
                }
                .onFailure {
                    Logger.e(
                        TAG,
                        it
                    ) { "Saving ${data.map { it.javaClass.simpleName }} failed: " }
                }
        }
    }

    suspend fun uploadData(data: BaseData): Result<List<BaseData>> = uploadData(listOf(data))

    private suspend fun getDataSetId() = dataRepository.awaitOrCreateCachedDataSetId {
        getUserDataSets()
            .flatMap { dataSets ->
                dataSets
                    .filter { it.uploadId != null }
                    .minByOrNull { it.uploadId!! }
                    ?.let {
                        Logger.d(TAG) { "Found existing data set with id: ${it.id}" }
                        Result.success(it)
                    }
                    ?: createDataSet(
                        newDataSet = NewDataSet(
                            client = ClientSoftware(
                                name = "org.tidepool.loop",
                                version = "TEST",
                            ),
                            dataSetType = "continuous",
                            timezone = TimeZone.getDefault().id,
                            timeZoneOffset = TimeZone.getDefault().rawOffset.milliseconds.inWholeMinutes.toInt(),
                            deviceManufacturers = listOf(
                                "test"
                            ),
                            deviceId = "test",
                            time = Clock.System.now(),
                            deduplicator = DeduplicatorDescriptor(
                                name = "org.tidepool.deduplicator.dataset.delete.origin",
                            ),
                            deviceTags = listOf(
                                DeviceTag.Bgm,
                                DeviceTag.Cgm,
                                DeviceTag.InsulinPump,
                            ),
                            deviceSerialNumber = "test",
                            timeProcessing = "none",
                        ),
                    )
            }.flatMap {
                Logger.d(TAG) { "Created data set: ${it.id}" }
                it.id?.let { Result.success(it) }
                    ?: Result.failure(IllegalStateException("No id"))
            }
    }

    fun clearUserDataSetId() {
        dataRepository.clearCachedDataSetId()
    }
}