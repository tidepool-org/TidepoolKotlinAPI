package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SummaryConfigDto(
    val schemaVersion: Int,
    val highGlucoseThreshold: Double,
    val veryHighGlucoseThreshold: Double,
    val lowGlucoseThreshold: Double,
    @SerialName("VeryLowGlucoseThreshold")
    val veryLowGlucoseThreshold: Double,
)