package org.tidepool.sdk.model.blob

import org.tidepool.sdk.dto.blob.BlobMetadataDto
import org.tidepool.sdk.dto.blob.BlobStatusDto
import java.time.Instant

data class BlobMetadata(
    val id: String,
    val userId: String,
    val digestMD5: String,
    val mediaType: String,
    val size: Long,
    val status: BlobStatus,
    val createdTime: Instant,
    val modifiedTime: Instant? = null,
    val deletedTime: Instant? = null,
    val revision: Int
)

enum class BlobStatus {
    Created,
    Available
}

// Conversion functions
internal fun BlobMetadataDto.toDomain() = BlobMetadata(
    id = id,
    userId = userId,
    digestMD5 = digestMD5,
    mediaType = mediaType,
    size = size,
    status = status.toDomain(),
    createdTime = createdTime,
    modifiedTime = modifiedTime,
    deletedTime = deletedTime,
    revision = revision
)

internal fun BlobStatusDto.toDomain() = when (this) {
    BlobStatusDto.Created   -> BlobStatus.Created
    BlobStatusDto.Available -> BlobStatus.Available
}

internal fun BlobStatus.toDto() = when (this) {
    BlobStatus.Created   -> BlobStatusDto.Created
    BlobStatus.Available -> BlobStatusDto.Available
}