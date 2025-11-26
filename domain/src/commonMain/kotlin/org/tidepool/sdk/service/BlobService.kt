package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.model.blob.BlobMetadata
import org.tidepool.sdk.model.blob.BlobStatus
import org.tidepool.sdk.model.blob.DeviceLogContent
import org.tidepool.sdk.model.blob.DeviceLogsMetadata
import org.tidepool.sdk.repository.BlobRepository
import kotlinx.datetime.Instant

class BlobService internal constructor(
    private val blobRepository: BlobRepository,
    private val tokenProvider: TokenProvider,
) {
    
    // Blob operations
    suspend fun listBlobs(
        userId: String,
        mediaType: String? = null,
        status: BlobStatus? = null
    ): Result<List<BlobMetadata>> = tokenProvider.getToken().flatMap {
        blobRepository.listBlobs(
            userId = userId,
            mediaType = mediaType,
            status = status,
            sessionToken = it,
        )
    }
    
//    suspend fun createBlob(
//        userId: String,
//        contentType: String,
//        digest: String,
//        content: ByteArray
//    ): Result<BlobMetadata> = tokenProvider.getToken().flatMap {
//        blobRepository.createBlob(
//            userId = userId,
//            contentType = contentType,
//            digest = digest,
//            content = content,
//            sessionToken = it,
//        )
//    }
    
    suspend fun deleteAllBlobs(userId: String): Result<Unit> = tokenProvider.getToken().flatMap {
        blobRepository.deleteAllBlobs(
            userId = userId,
            sessionToken = it,
        )
    }
    
    suspend fun getBlobMetadata(blobId: String): Result<BlobMetadata> =
        tokenProvider.getToken().flatMap {
            blobRepository.getBlobMetadata(
                blobId = blobId,
                sessionToken = it
            )
        }
    
    suspend fun deleteBlob(blobId: String): Result<Unit> = tokenProvider.getToken().flatMap {
        blobRepository.deleteBlob(
            blobId = blobId,
            sessionToken = it,
        )
    }
    
//    suspend fun getBlobContent(blobId: String): Result<ByteArray> =
//        tokenProvider.getToken().flatMap {
//            blobRepository.getBlobContent(
//                blobId = blobId,
//                sessionToken = it,
//            )
//        }
    
    // Device logs operations
    suspend fun uploadDeviceLogs(
        userId: String,
        digest: String,
        startAtTime: Instant,
        endAtTime: Instant,
        logs: List<DeviceLogContent>
    ): Result<DeviceLogsMetadata> = tokenProvider.getToken().flatMap {
        blobRepository.uploadDeviceLogs(
            userId = userId,
            digest = digest,
            startAtTime = startAtTime,
            endAtTime = endAtTime,
            logs = logs,
            sessionToken = it,
        )
    }
    
    suspend fun listDeviceLogs(
        userId: String,
        startAtTime: Instant,
        endAtTime: Instant
    ): Result<List<DeviceLogsMetadata>> = tokenProvider.getToken().flatMap {
        blobRepository.listDeviceLogs(
            userId = userId,
            startAtTime = startAtTime,
            endAtTime = endAtTime,
            sessionToken = it,
        )
    }
}