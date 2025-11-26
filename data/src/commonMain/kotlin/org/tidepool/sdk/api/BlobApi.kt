package org.tidepool.sdk.api

import org.tidepool.sdk.dto.blob.BlobMetadataDto
import org.tidepool.sdk.dto.blob.BlobStatusDto
import org.tidepool.sdk.dto.blob.DeviceLogContentDto
import org.tidepool.sdk.dto.blob.DeviceLogsMetadataDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface BlobApi {
    
    // Blob operations
    @GET("v1/users/{userId}/blobs")
    suspend fun listBlobs(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("mediaType") mediaType: String? = null,
        @Query("status") status: BlobStatusDto? = null,
    ): List<BlobMetadataDto>
    
//    @POST("v1/users/{userId}/blobs")
//    suspend fun createBlob(
//        @Header("X-Tidepool-Session-Token") sessionToken: String,
//        @Path("userId") userId: String,
//        @Header("Content-Type") contentType: String,
//        @Header("Digest") digest: String,
//        @Body content: RequestBody,
//    ): BlobMetadataDto
    
    @DELETE("v1/users/{userId}/blobs")
    suspend fun deleteAllBlobs(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
    )
    
    @GET("v1/blobs/{blobId}")
    suspend fun getBlobMetadata(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("blobId") blobId: String,
    ): BlobMetadataDto
    
    @DELETE("v1/blobs/{blobId}")
    suspend fun deleteBlob(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("blobId") blobId: String,
    )
    
//    @GET("v1/blobs/{blobId}/content")
//    suspend fun getBlobContent(
//        @Header("X-Tidepool-Session-Token") sessionToken: String,
//        @Path("blobId") blobId: String,
//    ): ResponseBody
    
    // Device logs operations
    @POST("v1/users/{userId}/device_logs")
    suspend fun uploadDeviceLogs(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Header("Digest") digest: String,
        @Header("X-Logs-Start-At-Time") startAtTime: String,
        @Header("X-Logs-End-At-Time") endAtTime: String,
        @Body logs: List<DeviceLogContentDto>,
    ): DeviceLogsMetadataDto
    
    @GET("v1/users/{userId}/device_logs")
    suspend fun listDeviceLogs(
        @Header("X-Tidepool-Session-Token") sessionToken: String,
        @Path("userId") userId: String,
        @Query("startAtTime") startAtTime: String,
        @Query("endAtTime") endAtTime: String,
    ): List<DeviceLogsMetadataDto>
}