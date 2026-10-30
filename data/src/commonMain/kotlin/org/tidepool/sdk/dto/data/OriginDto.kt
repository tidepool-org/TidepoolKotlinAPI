package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.Origin

@Serializable
data class OriginDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: String,
    @SerialName("version")
    val version: String,
)

internal fun OriginDto.toDomain(): Origin =
    Origin(
        id = id,
        name = name,
        type = type,
        version = version,
    )

internal fun Origin.toDto(): OriginDto =
    OriginDto(
        id = id,
        name = name,
        type = type,
        version = version,
    )
