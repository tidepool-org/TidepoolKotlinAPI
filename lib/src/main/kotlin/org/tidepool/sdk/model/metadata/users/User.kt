package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.dto.user.UserDto
import org.tidepool.sdk.model.metadata.Profile
import java.time.Instant

open class User(
    open val emailVerified: Boolean = false,
    open val emails: List<String>? = null,
    open val termsAccepted: Instant? = null,
    open val userId: String = "",
    open val username: String? = null,
    open val roles: List<String>? = null,
    open val createdTime: Instant? = null,
    open val createdUserId: String? = null,
    open val modifiedTime: Instant? = null,
    open val modifiedUserId: String? = null,
    open val deletedTime: Instant? = null,
    open val deletedUserId: String? = null,
    open val profile: Profile? = null,
)

internal fun UserDto.toDomain() = User(
    emailVerified = emailVerified,
    emails = emails ?: emptyList(),
    termsAccepted = termsAccepted,
    userId = userId,
    username = userName,
    roles = roles ?: emptyList(),
    createdTime = createdTime,
    createdUserId = createdUserId,
    modifiedTime = modifiedTime,
    modifiedUserId = modifiedUserId,
    deletedTime = deletedTime,
    deletedUserId = deletedUserId,
    profile = profile?.let {
        Profile(it.fullName)
    }
)