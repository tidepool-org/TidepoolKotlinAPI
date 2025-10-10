package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.Serializable

@Serializable
data class PrivateDto(
    val uploads: UploadsDto? = null,
) {
    
    @Serializable
    data class UploadsDto(
        val name: String? = null,
        val id: String? = null,
        val hash: String? = null,
    )
}