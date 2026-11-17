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

// Fields are nullable with no required ones: real error bodies vary (e.g. `meta` without a
// `deliveryType`), and this is decoded defensively from a caught exception's message rather than
// from a response the server contract guarantees the shape of.
@Serializable
data class ErrorDto(
    @SerialName("code")
    val code: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("detail")
    val detail: String? = null,
    @SerialName("source")
    val source: ErrorSourceDto? = null,
    @SerialName("meta")
    val meta: ErrorMetaDto? = null,
)

@Serializable
data class ErrorSourceDto(
    @SerialName("pointer")
    val pointer: String? = null,
)

@Serializable
data class ErrorMetaDto(
    @SerialName("type")
    val type: String? = null,
    @SerialName("deliveryType")
    val deliveryType: String? = null,
)