package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.blob.*
import org.tidepool.sdk.repository.BlobRepository
import java.time.Instant

class BlobService internal constructor(
    private val blobRepository: BlobRepository,
    private val tokenProvider: TokenProvider,
) {
    
    // Blob operations
    suspend fun listBlobs(
        userId: String,
        mediaType: String? = null,
        status: BlobStatus? = null
    ): Result<List<BlobMetadata>> = blobRepository.listBlobs(
        userId = userId,
        mediaType = mediaType,
        status = status?.toDto(),
        sessionToken = tokenProvider.getToken(),
    ).mapList { it.toDomain() }
    
    suspend fun createBlob(
        userId: String,
        contentType: String,
        digest: String,
        content: ByteArray
    ): Result<BlobMetadata> = blobRepository.createBlob(
        userId = userId,
        contentType = contentType,
        digest = digest,
        content = content,
        sessionToken = tokenProvider.getToken(),
    ).map { it.toDomain() }
    
    suspend fun deleteAllBlobs(userId: String): Result<Unit> = blobRepository.deleteAllBlobs(
        userId = userId,
        sessionToken = tokenProvider.getToken(),
    )
    
    suspend fun getBlobMetadata(blobId: String): Result<BlobMetadata> =
        blobRepository.getBlobMetadata(
            blobId = blobId,
            sessionToken = tokenProvider.getToken()
        ).map { it.toDomain() }
    
    suspend fun deleteBlob(blobId: String): Result<Unit> = blobRepository.deleteBlob(
        blobId = blobId,
        sessionToken = tokenProvider.getToken(),
    )
    
    suspend fun getBlobContent(blobId: String): Result<ByteArray> = blobRepository.getBlobContent(
        blobId = blobId,
        sessionToken = tokenProvider.getToken(),
    )
    
    // Device logs operations
    suspend fun uploadDeviceLogs(
        userId: String,
        digest: String,
        startAtTime: Instant,
        endAtTime: Instant,
        logs: List<DeviceLogContent>
    ): Result<DeviceLogsMetadata> = blobRepository.uploadDeviceLogs(
        userId = userId,
        digest = digest,
        startAtTime = startAtTime,
        endAtTime = endAtTime,
        logs = logs.map { it.toDto() },
        sessionToken = tokenProvider.getToken(),
    ).map { it.toDomain() }
    
    suspend fun listDeviceLogs(
        userId: String,
        startAtTime: Instant,
        endAtTime: Instant
    ): Result<List<DeviceLogsMetadata>> = blobRepository.listDeviceLogs(
        userId = userId,
        startAtTime = startAtTime,
        endAtTime = endAtTime,
        sessionToken = tokenProvider.getToken(),
    ).mapList { it.toDomain() }
}