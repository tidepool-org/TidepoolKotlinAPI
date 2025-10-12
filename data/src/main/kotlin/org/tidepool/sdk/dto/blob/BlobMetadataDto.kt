package org.tidepool.sdk.dto.blob

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.blob.BlobStatus
import java.time.Instant

@Serializable
@KonvertTo(org.tidepool.sdk.model.blob.BlobMetadata::class, mapFunctionName = "toDomain")
data class BlobMetadataDto(
    @SerialName("id")
    val id: String,
    @SerialName("userId")
    val userId: String,
    @SerialName("digestMD5")
    val digestMD5: String,
    @SerialName("mediaType")
    val mediaType: String,
    @SerialName("size")
    val size: Long,
    @SerialName("status")
    val status: BlobStatusDto,
    @Contextual
    @SerialName("createdTime")
    val createdTime: Instant,
    @Contextual
    @SerialName("modifiedTime")
    val modifiedTime: Instant? = null,
    @Contextual
    @SerialName("deletedTime")
    val deletedTime: Instant? = null,
    @SerialName("revision")
    val revision: Int
)

@Serializable
enum class BlobStatusDto {
    
    @kotlinx.serialization.SerialName("created")
    Created,
    
    @kotlinx.serialization.SerialName("available")
    Available
}

internal fun BlobStatusDto.toDomain() = when (this) {
    BlobStatusDto.Created   -> BlobStatus.Created
    BlobStatusDto.Available -> BlobStatus.Available
}

internal fun BlobStatus.toDto() = when (this) {
    BlobStatus.Created   -> BlobStatusDto.Created
    BlobStatus.Available -> BlobStatusDto.Available
}