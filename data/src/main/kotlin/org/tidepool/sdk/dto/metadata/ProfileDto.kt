package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.Serializable

@Serializable
open class ProfileDto(
    val fullName: String? = null,
)