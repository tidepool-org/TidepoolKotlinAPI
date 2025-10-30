package org.tidepool.sdk.service

import kotlinx.coroutines.CoroutineScope
import org.tidepool.sdk.AppLifecycleProvider
import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.repository.UserRepository
import kotlin.time.Duration
import java.time.Instant
import java.util.Collections.emptyList

class DataService internal constructor(
    private val dataRepository: DataRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
) {
    
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
                tokenProvider.getToken().flatMap {
                    dataRepository.uploadCachedData(
                        userId = userRepository.getCurrentUser(it).getOrThrow().userId,
                        sessionToken = it,
                    )
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
        dataSetId: String,
        data: List<BaseData>
    ): Result<List<BaseData>> =
        tokenProvider.getToken().flatMap {
            dataRepository.uploadDataToDataSet(
                dataSetId = dataSetId,
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
        data: List<BaseData>
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
    
    suspend fun uploadData(userId: String, data: List<BaseData>): Result<List<BaseData>> =
        tokenProvider.getToken().flatMap {
            dataRepository.uploadData(
                userId = userId,
                data = data,
                sessionToken = it,
            )
        }

    suspend fun uploadData(data: List<BaseData>): Result<List<BaseData>> =
        tokenProvider.getToken().flatMap { token ->
            userRepository.getCurrentUser(token).flatMap { user ->
                dataRepository.uploadData(
                    userId = user.userId,
                    data = data,
                    sessionToken = token,
                )
            }
        }
}