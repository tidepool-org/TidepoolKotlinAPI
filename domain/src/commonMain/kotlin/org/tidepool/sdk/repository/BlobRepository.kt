package org.tidepool.sdk.repository

import org.tidepool.sdk.model.blob.BlobMetadata
import org.tidepool.sdk.model.blob.BlobStatus
import org.tidepool.sdk.model.blob.DeviceLogContent
import org.tidepool.sdk.model.blob.DeviceLogsMetadata
import kotlinx.datetime.Instant

interface BlobRepository {
    
    // Blob operations
    suspend fun listBlobs(
        userId: String,
        mediaType: String? = null,
        status: BlobStatus? = null,
        sessionToken: String
    ): Result<List<BlobMetadata>>
    
//    suspend fun createBlob(
//        userId: String,
//        contentType: String,
//        digest: String,
//        content: ByteArray,
//        sessionToken: String
//    ): Result<BlobMetadata>
    
    suspend fun deleteAllBlobs(
        userId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun getBlobMetadata(
        blobId: String,
        sessionToken: String
    ): Result<BlobMetadata>
    
    suspend fun deleteBlob(
        blobId: String,
        sessionToken: String
    ): Result<Unit>
    
//    suspend fun getBlobContent(
//        blobId: String,
//        sessionToken: String
//    ): Result<ByteArray>
    
    // Device logs operations
    suspend fun uploadDeviceLogs(
        userId: String,
        digest: String,
        startAtTime: Instant,
        endAtTime: Instant,
        logs: List<DeviceLogContent>,
        sessionToken: String
    ): Result<DeviceLogsMetadata>
    
    suspend fun listDeviceLogs(
        userId: String,
        startAtTime: Instant,
        endAtTime: Instant,
        sessionToken: String
    ): Result<List<DeviceLogsMetadata>>
}