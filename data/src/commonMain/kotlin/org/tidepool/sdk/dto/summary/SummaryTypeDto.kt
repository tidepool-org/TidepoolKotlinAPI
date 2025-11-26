package org.tidepool.sdk.dto.summary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SummaryTypeDto {
    
    @SerialName("cgm")
    Cgm,
    @SerialName("bgm")
    Bgm,
    @SerialName("con")
    Con
}