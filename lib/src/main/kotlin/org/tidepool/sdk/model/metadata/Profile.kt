package org.tidepool.sdk.model.metadata

import org.tidepool.sdk.dto.metadata.ProfileDto

data class Profile(
    val fullName: String? = null,
)

internal fun Profile.toDto() = ProfileDto(
    fullName = fullName,
)

internal fun ProfileDto.toDomain() = Profile(
    fullName = fullName,
)