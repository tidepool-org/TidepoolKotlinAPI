package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ProfileDto(
    @SerialName("fullName")
    val fullName: String? = null,
)