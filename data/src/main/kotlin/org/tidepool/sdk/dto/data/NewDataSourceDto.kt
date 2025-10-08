package org.tidepool.sdk.dto.data

import kotlinx.serialization.Serializable

@Serializable
data class NewDataSourceDto(
    val providerName: String? = null,
    val providerSessionId: String? = null,
    val providerType: String? = null
)