package org.tidepool.sdk.model.metadata

import org.tidepool.sdk.dto.metadata.PrivateDto

data class Private(
    val uploads: Uploads? = null,
) {
    
    data class Uploads(
        val name: String? = null,
        val id: String? = null,
        val hash: String? = null,
    )
}

internal fun Private.toDto() = PrivateDto(
    uploads = uploads?.toDto(),
)

internal fun PrivateDto.toDomain() = Private(
    uploads = uploads?.toDomain(),
)

internal fun Private.Uploads.toDto() = PrivateDto.UploadsDto(
    name = name,
    id = id,
    hash = hash,
)

internal fun PrivateDto.UploadsDto.toDomain() = Private.Uploads(
    name = name,
    id = id,
    hash = hash,
)