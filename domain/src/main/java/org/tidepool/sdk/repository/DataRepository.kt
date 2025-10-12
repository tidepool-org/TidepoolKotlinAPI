package org.tidepool.sdk.repository

import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import java.time.Instant

interface DataRepository {

    suspend fun getDataForUser(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<DataType>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        dexcom: Boolean? = null,
        carelink: Boolean? = null,
        medtronic: Boolean? = null,
        sessionToken: String,
    ): Result<List<BaseData>>
    
    // Data Sets operations
    suspend fun getUserDataSets(
        userId: String,
        sessionToken: String,
        page: Int? = null,
        size: Int? = null,
    ): Result<List<DataSet>>
    
    suspend fun createDataSet(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String
    ): Result<DataSet>
    
    suspend fun getDataSet(
        dataSetId: String,
        sessionToken: String
    ): Result<DataSet>
    
    suspend fun updateDataSet(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String
    ): Result<DataSet>
    
    suspend fun deleteDataSet(
        dataSetId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun uploadDataToDataSet(
        dataSetId: String,
        data: List<BaseData>,
        sessionToken: String
    ): Result<List<BaseData>>
    
    suspend fun deleteDataSetData(
        dataSetId: String,
        sessionToken: String
    ): Result<Unit>
    
    // Legacy datasets operations
    suspend fun getUserDataSetsLegacy(
        userId: String,
        sessionToken: String
    ): Result<List<DataSet>>
    
    suspend fun createDataSetLegacy(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String
    ): Result<DataSet>
    
    suspend fun getDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ): Result<DataSet>
    
    suspend fun updateDataSetLegacy(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String
    ): Result<DataSet>
    
    suspend fun deleteDataSetLegacy(
        dataSetId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseData>,
        sessionToken: String
    ): Result<List<BaseData>>
    
    // Data Sources operations
    suspend fun getUserDataSources(
        userId: String,
        sessionToken: String
    ): Result<List<DataSource>>
    
    suspend fun createDataSource(
        userId: String,
        newDataSource: NewDataSource,
        sessionToken: String
    ): Result<DataSource>
    
    suspend fun deleteAllDataSources(
        userId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun getDataSource(
        dataSourceId: String,
        sessionToken: String
    ): Result<DataSource>
    
    suspend fun updateDataSource(
        dataSourceId: String,
        dataSource: DataSource,
        sessionToken: String
    ): Result<DataSource>
    
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
        types: List<DataType>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        sessionToken: String
    ): Result<List<BaseData>>
    
    suspend fun uploadData(
        userId: String,
        data: List<BaseData>,
        sessionToken: String
    ): Result<List<BaseData>>
}