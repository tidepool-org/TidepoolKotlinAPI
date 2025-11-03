package org.tidepool.sdk.service

import org.tidepool.sdk.Paginator
import org.tidepool.sdk.PaginatorImpl
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.repository.DataRepository
import java.time.Instant
import java.util.Collections.emptyList

class DataService internal constructor(
    private val dataRepository: DataRepository,
    private val tokenProvider: TokenProvider,
) {
    
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
    ): Result<List<BaseData>> = dataRepository.getDataForUser(
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
        sessionToken = tokenProvider.getToken()
    )
    
    // Data Sets operations
    suspend fun getUserDataSets(userId: String): Result<List<DataSet>> =
        dataRepository.getUserDataSets(
            userId = userId,
            sessionToken = tokenProvider.getToken(),
        )
    
    // Data Sets operations
    suspend fun getUserDataSetsPaginator(
        userId: String,
        pageSize: Int = 42,
        onPageLoadSuccess: suspend (List<DataSet>, Boolean) -> Unit,
        onPageLoadFailure: suspend (Throwable) -> Unit,
    ): Paginator<Int, List<DataSet>> = PaginatorImpl(
        initialKey = 0,
        onRequest = { page ->
            dataRepository.getUserDataSets(
                userId = userId,
                sessionToken = tokenProvider.getToken(),
                page = page,
                size = pageSize,
            )
        },
        getNextKey = { page, offset -> offset + page.size },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize }
    )
    
    suspend fun createDataSet(userId: String, newDataSet: NewDataSet): Result<DataSet> =
        dataRepository.createDataSet(
            userId = userId,
            newDataSet = newDataSet,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun getDataSet(dataSetId: String): Result<DataSet> =
        dataRepository.getDataSet(
            dataSetId = dataSetId,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun updateDataSet(dataSetId: String, dataSet: DataSet): Result<DataSet> =
        dataRepository.updateDataSet(
            dataSetId = dataSetId,
            dataSet = dataSet,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun deleteDataSet(dataSetId: String): Result<Unit> =
        dataRepository.deleteDataSet(
            dataSetId = dataSetId,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun uploadDataToDataSet(
        dataSetId: String,
        data: List<BaseData>
    ): Result<List<BaseData>> =
        dataRepository.uploadDataToDataSet(
            dataSetId = dataSetId,
            data = data,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun deleteDataSetData(dataSetId: String): Result<Unit> =
        dataRepository.deleteDataSetData(
            dataSetId = dataSetId,
            sessionToken = tokenProvider.getToken()
        )
    
    // Legacy datasets operations
    @Deprecated("Use getUserDataSets instead", ReplaceWith("getUserDataSets"))
    suspend fun getUserDataSetsLegacy(userId: String): Result<List<DataSet>> =
        dataRepository.getUserDataSetsLegacy(
            userId = userId,
            sessionToken = tokenProvider.getToken()
        )
    
    @Deprecated("Use createDataSet instead", ReplaceWith("createDataSet"))
    suspend fun createDataSetLegacy(userId: String, newDataSet: NewDataSet): Result<DataSet> =
        dataRepository.createDataSetLegacy(
            userId = userId,
            newDataSet = newDataSet,
            sessionToken = tokenProvider.getToken()
        )
    
    @Deprecated("Use getDataSet instead", ReplaceWith("getDataSet"))
    suspend fun getDataSetLegacy(dataSetId: String): Result<DataSet> =
        dataRepository.getDataSetLegacy(
            dataSetId = dataSetId,
            sessionToken = tokenProvider.getToken()
        )
    
    @Deprecated("Use updateDataSet instead", ReplaceWith("updateDataSet"))
    suspend fun updateDataSetLegacy(dataSetId: String, dataSet: DataSet): Result<DataSet> =
        dataRepository.updateDataSetLegacy(
            dataSetId = dataSetId,
            dataSet = dataSet,
            sessionToken = tokenProvider.getToken()
        )
    
    @Deprecated("Use deleteDataSet instead", ReplaceWith("deleteDataSet"))
    suspend fun deleteDataSetLegacy(dataSetId: String): Result<Unit> =
        dataRepository.deleteDataSetLegacy(
            dataSetId = dataSetId,
            sessionToken = tokenProvider.getToken()
        )
    
    @Deprecated("Use uploadDataToDataSet instead", ReplaceWith("uploadDataToDataSet"))
    suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseData>
    ): Result<List<BaseData>> =
        dataRepository.uploadDataToDataSetLegacy(
            dataSetId = dataSetId,
            data = data,
            sessionToken = tokenProvider.getToken()
        )
    
    // Data Sources operations
    suspend fun getUserDataSources(userId: String): Result<List<DataSource>> =
        dataRepository.getUserDataSources(
            userId = userId,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun getUserDataSourcesPaginator(
        userId: String,
        pageSize: Int = 42,
        onPageLoadSuccess: suspend (List<DataSource>, Boolean) -> Unit,
        onPageLoadFailure: suspend (Throwable) -> Unit,
    ): Paginator<Int, List<DataSource>> = PaginatorImpl(
        initialKey = 0,
        onRequest = { page ->
            dataRepository.getUserDataSources(
                userId = userId,
                sessionToken = tokenProvider.getToken()
            )
        },
        getNextKey = { page, offset -> offset + page.size },
        onSuccess = onPageLoadSuccess,
        onFailure = onPageLoadFailure,
        endReached = { page, _ -> page.size < pageSize },
    )
    
    suspend fun createDataSource(userId: String, newDataSource: NewDataSource): Result<DataSource> =
        dataRepository.createDataSource(
            userId = userId,
            newDataSource = newDataSource,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun deleteAllDataSources(userId: String): Result<Unit> =
        dataRepository.deleteAllDataSources(
            userId = userId,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun getDataSource(dataSourceId: String): Result<DataSource> =
        dataRepository.getDataSource(
            dataSourceId = dataSourceId,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun updateDataSource(dataSourceId: String, dataSource: DataSource): Result<DataSource> =
        dataRepository.updateDataSource(
            dataSourceId = dataSourceId,
            dataSource = dataSource,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun deleteDataSource(dataSourceId: String): Result<Unit> =
        dataRepository.deleteDataSource(
            dataSourceId = dataSourceId,
            sessionToken = tokenProvider.getToken()
        )
    
    // Additional data operations
    suspend fun deleteAllUserData(userId: String): Result<Unit> =
        dataRepository.deleteAllUserData(
            userId = userId,
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun getData(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<DataType>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
    ): Result<List<BaseData>> = dataRepository.getData(
        userId = userId,
        uploadId = uploadId,
        deviceId = deviceId,
        types = types,
        startDate = startDate,
        endDate = endDate,
        latest = latest,
        sessionToken = tokenProvider.getToken()
    )
    
    suspend fun uploadData(userId: String, data: List<BaseData>): Result<List<BaseData>> =
        dataRepository.uploadData(
            userId = userId,
            data = data,
            sessionToken = tokenProvider.getToken()
        )
}