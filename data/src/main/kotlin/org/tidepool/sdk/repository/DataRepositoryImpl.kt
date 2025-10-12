package org.tidepool.sdk.repository

import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.dto.clinic.fromDomain
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.toDomain
import org.tidepool.sdk.dto.data.toDto
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.dto.data.DataSetDto
import org.tidepool.sdk.dto.data.DataSourceDto
import org.tidepool.sdk.dto.data.NewDataSetDto
import org.tidepool.sdk.dto.data.NewDataSourceDto
import org.tidepool.sdk.dto.data.fromDomain
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant
import kotlin.collections.toTypedArray

class DataRepositoryImpl(
    val dataApi: DataApi,
) : DataRepository {
    
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
        sessionToken: String
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
            startDate = startDate,
            endDate = endDate,
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
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSet(
            sessionToken = sessionToken,
            userId = userId,
            newDataSet = NewDataSetDto.fromDomain(newDataSet),
        )
    }.map { it.toDomain() }
    
    override suspend fun getDataSet(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSet(sessionToken, dataSetId)
    }.map { it.toDomain() }
    
    override suspend fun updateDataSet(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSet(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            dataSet = DataSetDto.fromDomain(dataSet),
        )
    }.map { it.toDomain() }
    
    override suspend fun deleteDataSet(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSet(sessionToken, dataSetId)
    }
    
    override suspend fun uploadDataToDataSet(
        dataSetId: String,
        data: List<BaseData>,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.uploadDataToDataSet(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            data = data.map { BaseDataDto.fromDomain(it) },
        )
    }.mapList { it.toDomain() }
    
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
    }.mapList { it.toDomain() }
    
    override suspend fun createDataSetLegacy(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSetLegacy(
            sessionToken = sessionToken,
            userId = userId,
            newDataSet = NewDataSetDto.fromDomain(newDataSet),
        )
    }.map { it.toDomain() }
    
    override suspend fun getDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getDataSetLegacy(sessionToken, dataSetId)
    }.map { it.toDomain() }
    
    override suspend fun updateDataSetLegacy(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSetLegacy(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            dataSet = DataSetDto.fromDomain(dataSet),
        )
    }.map { it.toDomain() }
    
    override suspend fun deleteDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.deleteDataSetLegacy(sessionToken, dataSetId)
    }
    
    override suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseData>,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.uploadDataToDataSetLegacy(
            sessionToken = sessionToken,
            dataSetId = dataSetId,
            data = data.map { BaseDataDto.fromDomain(it) }
        )
    }.mapList { it.toDomain() }
    
    // Data Sources operations
    override suspend fun getUserDataSources(
        userId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.getUserDataSources(sessionToken, userId)
    }.mapList { it.toDomain() }
    
    override suspend fun createDataSource(
        userId: String,
        newDataSource: NewDataSource,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.createDataSource(
            sessionToken = sessionToken,
            userId = userId,
            newDataSource = NewDataSourceDto.fromDomain(newDataSource)
        )
    }.map { it.toDomain() }
    
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
    }.map { it.toDomain() }
    
    override suspend fun updateDataSource(
        dataSourceId: String,
        dataSource: DataSource,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.updateDataSource(
            sessionToken = sessionToken,
            dataSourceId = dataSourceId,
            dataSource = DataSourceDto.fromDomain(dataSource)
        )
    }.map { it.toDomain() }
    
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
        types: List<DataType>?,
        startDate: Instant?,
        endDate: Instant?,
        latest: Boolean?,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        val typesParam = types
            ?.map { it.toDto() }
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
    }.mapList { it.toDomain() }
    
    override suspend fun uploadData(
        userId: String,
        data: List<BaseData>,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        dataApi.uploadData(
            sessionToken = sessionToken,
            userId = userId,
            data = data.map { BaseDataDto.fromDomain(it) }
        )
    }.mapList { it.toDomain() }
}