package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.SummaryConfig

@Serializable
data class SummaryConfigDto(
    @SerialName("schemaVersion")
    val schemaVersion: Int,
    @SerialName("highGlucoseThreshold")
    val highGlucoseThreshold: Double,
    @SerialName("veryHighGlucoseThreshold")
    val veryHighGlucoseThreshold: Double,
    @SerialName("lowGlucoseThreshold")
    val lowGlucoseThreshold: Double,
    @SerialName("VeryLowGlucoseThreshold")
    val veryLowGlucoseThreshold: Double,
)

fun SummaryConfigDto.toDomain(): SummaryConfig = SummaryConfig(
    schemaVersion = schemaVersion,
    highGlucoseThreshold = highGlucoseThreshold,
    veryHighGlucoseThreshold = veryHighGlucoseThreshold,
    lowGlucoseThreshold = lowGlucoseThreshold,
    veryLowGlucoseThreshold = veryLowGlucoseThreshold
)
