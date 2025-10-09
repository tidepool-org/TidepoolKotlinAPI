package org.tidepool.sdk.dto.blob

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class BlobMetadataDto(
    val id: String,
    val userId: String,
    val digestMD5: String,
    val mediaType: String,
    val size: Long,
    val status: BlobStatusDto,
    @Contextual val createdTime: Instant,
    @Contextual val modifiedTime: Instant? = null,
    @Contextual val deletedTime: Instant? = null,
    val revision: Int
)

@Serializable
enum class BlobStatusDto {
    
    @kotlinx.serialization.SerialName("created")
    Created,
    
    @kotlinx.serialization.SerialName("available")
    Available
}