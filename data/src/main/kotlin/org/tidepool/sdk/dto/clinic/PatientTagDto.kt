package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Serializable

@Serializable
data class PatientTagDto(
    val id: String,
    val name: String,
)