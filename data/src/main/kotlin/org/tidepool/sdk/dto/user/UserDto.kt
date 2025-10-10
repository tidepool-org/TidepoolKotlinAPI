package org.tidepool.sdk.dto.user

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.tidepool.sdk.dto.metadata.UserProfileDto
import java.time.Instant

@Serializable
open class UserDto(
    val emailVerified: Boolean = false,
    val emails: List<String>? = null,
    @Contextual val termsAccepted: Instant? = null,
    @SerialName("userid")
    val userId: String = "",
    @SerialName("username")
    val userName: String? = null,
    val roles: List<String>? = null,
    @Contextual val createdTime: Instant? = null,
    val createdUserId: String? = null,
    @Contextual val modifiedTime: Instant? = null,
    val modifiedUserId: String? = null,
    @Contextual val deletedTime: Instant? = null,
    val deletedUserId: String? = null,
    val profile: UserProfileDto? = null,
)
