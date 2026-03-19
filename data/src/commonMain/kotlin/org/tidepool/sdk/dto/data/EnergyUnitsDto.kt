package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EnergyUnitsDto {
    @SerialName("calories")
    Calories,

    @SerialName("joules")
    Joules,

    @SerialName("kilocalories")
    Kilocalories,

    @SerialName("kilojoules")
    Kilojoules,
}

