package org.tidepool.sdk.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResponseDto<T>(
    @SerialName("data")
    val data: List<T> = emptyList(),
    @SerialName("errors")
    val errors: List<ErrorDto> = emptyList(),
)

@Serializable
data class ErrorDto(
    @SerialName("code")
    val code: String,
    @SerialName("title")
    val title: String,
    @SerialName("detail")
    val detail: String,
    @SerialName("source")
    val source: ErrorSourceDto,
    @SerialName("meta")
    val meta: ErrorMetaDto,
)

@Serializable
data class ErrorSourceDto(
    @SerialName("pointer")
    val pointer: String,
)

@Serializable
data class ErrorMetaDto(
    @SerialName("type")
    val type: String,
    @SerialName("deliveryType")
    val deliveryType: String,
)