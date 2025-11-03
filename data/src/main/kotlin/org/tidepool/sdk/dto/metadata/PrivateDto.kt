package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrivateDto(
    @SerialName("uploads")
    val uploads: UploadsDto? = null,
) {
    
    @Serializable
    data class UploadsDto(
        @SerialName("name")
        val name: String? = null,
        @SerialName("id")
        val id: String? = null,
        @SerialName("hash")
        val hash: String? = null,
    )
}