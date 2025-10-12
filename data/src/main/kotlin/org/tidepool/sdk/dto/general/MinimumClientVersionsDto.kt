package org.tidepool.sdk.dto.general

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.general.MinimumClientVersions

@Serializable
@KonvertTo(MinimumClientVersions::class, mapFunctionName = "toDomain")
data class MinimumClientVersionsDto(
    val versions: VersionsDto
) {
    
    @Serializable
    @KonvertTo(MinimumClientVersions.Versions::class, mapFunctionName = "toDomain")
    data class VersionsDto(
        val uploaderMinimum: String
    )
}