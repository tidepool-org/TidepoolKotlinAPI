package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NutrientUnitDto {
    @SerialName("grams")
    Grams,
}