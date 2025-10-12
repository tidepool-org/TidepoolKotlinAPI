package org.tidepool.sdk.model.blob

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