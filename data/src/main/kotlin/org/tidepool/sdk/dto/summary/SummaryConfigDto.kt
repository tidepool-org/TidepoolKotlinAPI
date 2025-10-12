package org.tidepool.sdk.dto.summary

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.summary.SummaryConfig

@Serializable
@KonvertTo(SummaryConfig::class, mapFunctionName = "toDomain")
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