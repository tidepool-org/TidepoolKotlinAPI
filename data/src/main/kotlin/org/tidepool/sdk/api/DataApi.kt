package org.tidepool.sdk.api

import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.DataSetDto
import org.tidepool.sdk.dto.data.DataSourceDto
import org.tidepool.sdk.dto.data.NewDataSetDto
import org.tidepool.sdk.dto.data.NewDataSourceDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Instant

interface DataApi {
    
    @GET("/data/{userId}")
    suspend fun getDataForUser(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("uploadId") uploadId: String? = null,
        @Query("deviceId") deviceId: String? = null,
        @Query("type", encoded = true) types: CommaSeparatedArray<BaseDataDto.DataTypeDto>? = null,
        @Query("startDate") startDate: Instant? = null,
        @Query("endDate") endDate: Instant? = null,
        @Query("latest") latest: Boolean? = null,
        @Query("dexcom") dexcom: Boolean? = null,
        @Query("carelink") carelink: Boolean? = null,
        @Query("medtronic") medtronic: Boolean? = null,
    ): List<BaseDataDto>
    
    // Data Sets endpoints
    @GET("/v1/users/{userId}/data_sets")
    suspend fun getUserDataSets(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): List<DataSetDto>
    
    @POST("/v1/users/{userId}/data_sets")
    suspend fun createDataSet(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body newDataSet: NewDataSetDto
    ): DataSetDto
    
    @GET("/v1/data_sets/{dataSetId}")
    suspend fun getDataSet(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String
    ): DataSetDto
    
    @PUT("/v1/data_sets/{dataSetId}")
    suspend fun updateDataSet(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
        @Body dataSet: DataSetDto
    ): DataSetDto
    
    @DELETE("/v1/data_sets/{dataSetId}")
    suspend fun deleteDataSet(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String
    )
    
    @POST("/v1/data_sets/{dataSetId}/data")
    suspend fun uploadDataToDataSet(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
        @Body data: List<BaseDataDto>,
    ): List<BaseDataDto>
    
    @DELETE("/v1/data_sets/{dataSetId}/data")
    suspend fun deleteDataSetData(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
    )
    
    // Legacy datasets endpoints
    @GET("/v1/users/{userId}/datasets")
    suspend fun getUserDataSetsLegacy(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
    ): List<DataSetDto>
    
    @POST("/v1/users/{userId}/datasets")
    suspend fun createDataSetLegacy(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body newDataSet: NewDataSetDto,
    ): DataSetDto
    
    @GET("/v1/datasets/{dataSetId}")
    suspend fun getDataSetLegacy(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
    ): DataSetDto
    
    @PUT("/v1/datasets/{dataSetId}")
    suspend fun updateDataSetLegacy(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
        @Body dataSet: DataSetDto
    ): DataSetDto
    
    @DELETE("/v1/datasets/{dataSetId}")
    suspend fun deleteDataSetLegacy(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
    )
    
    @POST("/v1/datasets/{dataSetId}/data")
    suspend fun uploadDataToDataSetLegacy(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSetId") dataSetId: String,
        @Body data: List<BaseDataDto>,
    ): List<BaseDataDto>
    
    // Data Sources endpoints
    @GET("/v1/users/{userId}/data_sources")
    suspend fun getUserDataSources(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): List<DataSourceDto>
    
    @POST("/v1/users/{userId}/data_sources")
    suspend fun createDataSource(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body newDataSource: NewDataSourceDto,
    ): DataSourceDto
    
    @DELETE("/v1/users/{userId}/data_sources")
    suspend fun deleteAllDataSources(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
    )
    
    @GET("/v1/data_sources/{dataSourceId}")
    suspend fun getDataSource(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSourceId") dataSourceId: String,
    ): DataSourceDto
    
    @PUT("/v1/data_sources/{dataSourceId}")
    suspend fun updateDataSource(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSourceId") dataSourceId: String,
        @Body dataSource: DataSourceDto,
    ): DataSourceDto
    
    @DELETE("/v1/data_sources/{dataSourceId}")
    suspend fun deleteDataSource(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("dataSourceId") dataSourceId: String,
    )
    
    // Additional data endpoints
    @DELETE("/v1/users/{userId}/data")
    suspend fun deleteAllUserData(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
    )
    
    @GET("/v1/users/{userId}/data")
    suspend fun getData(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("uploadId") uploadId: String? = null,
        @Query("deviceId") deviceId: String? = null,
        @Query("type", encoded = true) types: CommaSeparatedArray<BaseDataDto.DataTypeDto>? = null,
        @Query("startDate") startDate: Instant? = null,
        @Query("endDate") endDate: Instant? = null,
        @Query("latest") latest: Boolean? = null,
    ): List<BaseDataDto>
    
    @POST("/v1/users/{userId}/data")
    suspend fun uploadData(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Body data: List<BaseDataDto>,
    ): List<BaseDataDto>
    
    class CommaSeparatedArray<T>(private vararg val types: T) {
        
        override fun toString(): String {
            var result = ""
            for (type in types) {
                result += "$type,"
            }
            return result.substring(0, result.lastIndex)
        }
    }
}