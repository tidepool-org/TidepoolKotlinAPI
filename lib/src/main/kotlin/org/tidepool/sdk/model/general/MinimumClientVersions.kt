package org.tidepool.sdk.model.general

import org.tidepool.sdk.dto.general.MinimumClientVersionsDto

data class MinimumClientVersions(
    val uploaderMinimumVersion: String
)

internal fun MinimumClientVersionsDto.toDomain(): MinimumClientVersions = MinimumClientVersions(
    uploaderMinimumVersion = versions.uploaderMinimum,
)