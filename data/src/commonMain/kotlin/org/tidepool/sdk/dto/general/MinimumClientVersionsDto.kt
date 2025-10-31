package org.tidepool.sdk.dto.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.general.MinimumClientVersions

@Serializable
data class MinimumClientVersionsDto(
    @SerialName("minimumClientVersions")
    val versions: VersionsDto
) {
    
    @Serializable
    data class VersionsDto(
        val uploaderMinimum: String
    )
}

fun MinimumClientVersionsDto.toDomain() = MinimumClientVersions(
    versions = versions.toDomain(),
)

fun MinimumClientVersionsDto.VersionsDto.toDomain() = MinimumClientVersions.Versions(
    uploaderMinimum = uploaderMinimum,
)