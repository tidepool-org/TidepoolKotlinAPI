package org.tidepool.sdk.repository

import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import kotlinx.datetime.Instant

interface DataRepository {

    /**
     * Returns the cached data set id if available. If not available, ensures that at most one
     * coroutine performs the discovery/creation via [create], while all other callers wait for
     * the result. This provides a three-state behavior: Uninitialized, Loading, and Ready.
     */
    suspend fun awaitOrCreateCachedDataSetId(
        create: suspend () -> Result<String>,
    ): Result<String>

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
        data: List<BaseData>
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
    
    /**
     * Uploads one batch of locally cached data (bounded by the per-type limits enforced in the
     * implementation) and removes the uploaded rows from the local outbox on success.
     *
     * @return on success, `true` when any data type's batch came back at its limit - meaning
     * more cached data may still be waiting and the caller should call again - or `false` once
     * the outbox is drained (or was already empty).
     */
    suspend fun uploadCachedData(
        sessionToken: String,
        dataSetId: String,
    ): Result<Boolean>

    /** Whether any record that [uploadCachedData] sends is still waiting in the outbox. */
    suspend fun hasCachedData(): Boolean

    fun clearCachedDataSetId()
}