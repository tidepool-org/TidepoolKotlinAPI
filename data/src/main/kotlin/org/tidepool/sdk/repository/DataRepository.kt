package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.DataSetDto
import org.tidepool.sdk.dto.data.DataSourceDto
import org.tidepool.sdk.dto.data.NewDataSetDto
import org.tidepool.sdk.dto.data.NewDataSourceDto
import java.time.Instant

interface DataRepository {

    suspend fun getDataForUser(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<BaseDataDto.DataTypeDto>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        dexcom: Boolean? = null,
        carelink: Boolean? = null,
        medtronic: Boolean? = null,
        sessionToken: String,
    ): Result<List<BaseDataDto>>
    
    // Data Sets operations
    suspend fun getUserDataSets(
        userId: String,
        sessionToken: String,
        page: Int? = null,
        size: Int? = null,
    ): Result<List<DataSetDto>>
    
    suspend fun createDataSet(
        userId: String,
        newDataSet: NewDataSetDto,
        sessionToken: String
    ): Result<DataSetDto>
    
    suspend fun getDataSet(
        dataSetId: String,
        sessionToken: String
    ): Result<DataSetDto>
    
    suspend fun updateDataSet(
        dataSetId: String,
        dataSet: DataSetDto,
        sessionToken: String
    ): Result<DataSetDto>
    
    suspend fun deleteDataSet(
        dataSetId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun uploadDataToDataSet(
        dataSetId: String,
        data: List<BaseDataDto>,
        sessionToken: String
    ): Result<List<BaseDataDto>>
    
    suspend fun deleteDataSetData(
        dataSetId: String,
        sessionToken: String
    ): Result<Unit>
    
    // Legacy datasets operations
    suspend fun getUserDataSetsLegacy(
        userId: String,
        sessionToken: String
    ): Result<List<DataSetDto>>
    
    suspend fun createDataSetLegacy(
        userId: String,
        newDataSet: NewDataSetDto,
        sessionToken: String
    ): Result<DataSetDto>
    
    suspend fun getDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ): Result<DataSetDto>
    
    suspend fun updateDataSetLegacy(
        dataSetId: String,
        dataSet: DataSetDto,
        sessionToken: String
    ): Result<DataSetDto>
    
    suspend fun deleteDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseDataDto>,
        sessionToken: String
    ): Result<List<BaseDataDto>>
    
    // Data Sources operations
    suspend fun getUserDataSources(
        userId: String,
        sessionToken: String
    ): Result<List<DataSourceDto>>
    
    suspend fun createDataSource(
        userId: String,
        newDataSource: NewDataSourceDto,
        sessionToken: String
    ): Result<DataSourceDto>
    
    suspend fun deleteAllDataSources(
        userId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun getDataSource(
        dataSourceId: String,
        sessionToken: String
    ): Result<DataSourceDto>
    
    suspend fun updateDataSource(
        dataSourceId: String,
        dataSource: DataSourceDto,
        sessionToken: String
    ): Result<DataSourceDto>
    
    suspend fun deleteDataSource(
        dataSourceId: String,
        sessionToken: String
    ): Result<Unit>
    
    // Additional data operations
    suspend fun deleteAllUserData(
        userId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun getData(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<BaseDataDto.DataTypeDto>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        sessionToken: String
    ): Result<List<BaseDataDto>>
    
    suspend fun uploadData(
        userId: String,
        data: List<BaseDataDto>,
        sessionToken: String
    ): Result<List<BaseDataDto>>
}