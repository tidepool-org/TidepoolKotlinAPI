package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.DataSetDto
import org.tidepool.sdk.dto.data.DataSourceDto
import org.tidepool.sdk.dto.data.NewDataSetDto
import org.tidepool.sdk.dto.data.NewDataSourceDto
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant

class DataRepositoryImpl(
    private val dataApi: DataApi,
) : DataRepository {
    
    override suspend fun getDataForUser(
        userId: String,
        uploadId: String?,
        deviceId: String?,
        types: List<BaseDataDto.DataTypeDto>?,
        startDate: Instant?,
        endDate: Instant?,
        latest: Boolean?,
        dexcom: Boolean?,
        carelink: Boolean?,
        medtronic: Boolean?,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        val typesParam = types
            ?.toTypedArray()
            ?.let { DataApi.CommaSeparatedArray(*it) }
        
        dataApi.getDataForUser(
            sessionToken = sessionToken,
            userId = userId,
            uploadId = uploadId,
            deviceId = deviceId,
            types = typesParam,
            startDate = startDate,
            endDate = endDate,
            latest = latest,
            dexcom = dexcom,
            carelink = carelink,
            medtronic = medtronic
        )
    }
    
    // Data Sets operations
    override suspend fun getUserDataSets(
        userId: String,
        sessionToken: String,
        page: Int?,
        size: Int?,
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSets(sessionToken, userId)
    }
    
    override suspend fun createDataSet(
        userId: String,
        newDataSet: NewDataSetDto,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSet(sessionToken, userId, newDataSet)
    }
    
    override suspend fun getDataSet(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSet(sessionToken, dataSetId)
    }
    
    override suspend fun updateDataSet(
        dataSetId: String,
        dataSet: DataSetDto,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSet(sessionToken, dataSetId, dataSet)
    }
    
    override suspend fun deleteDataSet(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSet(sessionToken, dataSetId)
    }
    
    override suspend fun uploadDataToDataSet(
        dataSetId: String,
        data: List<BaseDataDto>,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.uploadDataToDataSet(sessionToken, dataSetId, data)
    }
    
    override suspend fun deleteDataSetData(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSetData(sessionToken, dataSetId)
    }
    
    // Legacy datasets operations
    override suspend fun getUserDataSetsLegacy(
        userId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSetsLegacy(sessionToken, userId)
    }
    
    override suspend fun createDataSetLegacy(
        userId: String,
        newDataSet: NewDataSetDto,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSetLegacy(sessionToken, userId, newDataSet)
    }
    
    override suspend fun getDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSetLegacy(sessionToken, dataSetId)
    }
    
    override suspend fun updateDataSetLegacy(
        dataSetId: String,
        dataSet: DataSetDto,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSetLegacy(sessionToken, dataSetId, dataSet)
    }
    
    override suspend fun deleteDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSetLegacy(sessionToken, dataSetId)
    }
    
    override suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseDataDto>,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.uploadDataToDataSetLegacy(sessionToken, dataSetId, data)
    }
    
    // Data Sources operations
    override suspend fun getUserDataSources(
        userId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSources(sessionToken, userId)
    }
    
    override suspend fun createDataSource(
        userId: String,
        newDataSource: NewDataSourceDto,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSource(sessionToken, userId, newDataSource)
    }
    
    override suspend fun deleteAllDataSources(
        userId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteAllDataSources(sessionToken, userId)
    }
    
    override suspend fun getDataSource(
        dataSourceId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSource(sessionToken, dataSourceId)
    }
    
    override suspend fun updateDataSource(
        dataSourceId: String,
        dataSource: DataSourceDto,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSource(sessionToken, dataSourceId, dataSource)
    }
    
    override suspend fun deleteDataSource(
        dataSourceId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSource(sessionToken, dataSourceId)
    }
    
    // Additional data operations
    override suspend fun deleteAllUserData(
        userId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteAllUserData(sessionToken, userId)
    }
    
    override suspend fun getData(
        userId: String,
        uploadId: String?,
        deviceId: String?,
        types: List<BaseDataDto.DataTypeDto>?,
        startDate: Instant?,
        endDate: Instant?,
        latest: Boolean?,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        val typesParam = types
            ?.toTypedArray()
            ?.let { DataApi.CommaSeparatedArray(*it) }
        
        dataApi.getData(
            sessionToken = sessionToken,
            userId = userId,
            uploadId = uploadId,
            deviceId = deviceId,
            types = typesParam,
            startDate = startDate,
            endDate = endDate,
            latest = latest
        )
    }
    
    override suspend fun uploadData(
        userId: String,
        data: List<BaseDataDto>,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.uploadData(sessionToken, userId, data)
    }
}