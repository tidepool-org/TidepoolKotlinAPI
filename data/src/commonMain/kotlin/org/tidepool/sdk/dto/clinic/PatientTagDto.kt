package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.PatientTag

@Serializable
data class PatientTagDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
)

fun PatientTagDto.toDomain(): PatientTag = PatientTag(
    id = id,
    name = name
)

fun PatientTag.toDto(): PatientTagDto = PatientTagDto(
    id = id,
    name = name
)