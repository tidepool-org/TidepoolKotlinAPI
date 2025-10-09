package org.tidepool.sdk.repository

import okhttp3.ResponseBody
import org.tidepool.sdk.dto.blob.BlobMetadataDto
import org.tidepool.sdk.dto.blob.BlobStatusDto
import org.tidepool.sdk.dto.blob.DeviceLogContentDto
import org.tidepool.sdk.dto.blob.DeviceLogsMetadataDto
import java.time.Instant

interface BlobRepository {
    
    // Blob operations
    suspend fun listBlobs(
        userId: String,
        mediaType: String? = null,
        status: BlobStatusDto? = null,
        sessionToken: String
    ): Result<List<BlobMetadataDto>>
    
    suspend fun createBlob(
        userId: String,
        contentType: String,
        digest: String,
        content: ByteArray,
        sessionToken: String
    ): Result<BlobMetadataDto>
    
    suspend fun deleteAllBlobs(
        userId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun getBlobMetadata(
        blobId: String,
        sessionToken: String
    ): Result<BlobMetadataDto>
    
    suspend fun deleteBlob(
        blobId: String,
        sessionToken: String
    ): Result<Unit>
    
    suspend fun getBlobContent(
        blobId: String,
        sessionToken: String
    ): Result<ByteArray>
    
    // Device logs operations
    suspend fun uploadDeviceLogs(
        userId: String,
        digest: String,
        startAtTime: Instant,
        endAtTime: Instant,
        logs: List<DeviceLogContentDto>,
        sessionToken: String
    ): Result<DeviceLogsMetadataDto>
    
    suspend fun listDeviceLogs(
        userId: String,
        startAtTime: Instant,
        endAtTime: Instant,
        sessionToken: String
    ): Result<List<DeviceLogsMetadataDto>>
}