package org.tidepool.sdk.dto.general

import kotlinx.serialization.Serializable

@Serializable
data class MinimumClientVersionsDto(
    val versions: VersionsDto
) {
    
    @Serializable
    data class VersionsDto(
        val uploaderMinimum: String
    )
}