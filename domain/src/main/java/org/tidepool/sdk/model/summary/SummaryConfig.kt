package org.tidepool.sdk.model.summary

data class SummaryConfig(
    val schemaVersion: Int,
    val highGlucoseThreshold: Double,
    val veryHighGlucoseThreshold: Double,
    val lowGlucoseThreshold: Double,
    val veryLowGlucoseThreshold: Double
)