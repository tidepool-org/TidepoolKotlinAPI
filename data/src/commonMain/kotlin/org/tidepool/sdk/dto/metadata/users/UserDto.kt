package org.tidepool.sdk.dto.metadata.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.tidepool.sdk.dto.metadata.UserProfileDto
import kotlinx.datetime.Instant

@Serializable
open class UserDto(
    @SerialName("emailVerified")
    val emailVerified: Boolean = false,
    @SerialName("emails")
    val emails: List<String>? = null,
    @Contextual
    @SerialName("termsAccepted")
    val termsAccepted: Instant? = null,
    @SerialName("userid")
    val userId: String = "",
    @SerialName("username")
    val username: String? = null,
    @SerialName("roles")
    val roles: List<String>? = null,
    @Contextual
    @SerialName("createdTime")
    val createdTime: Instant? = null,
    @SerialName("createdUserId")
    val createdUserId: String? = null,
    @Contextual
    @SerialName("modifiedTime")
    val modifiedTime: Instant? = null,
    @SerialName("modifiedUserId")
    val modifiedUserId: String? = null,
    @Contextual
    @SerialName("deletedTime")
    val deletedTime: Instant? = null,
    @SerialName("deletedUserId")
    val deletedUserId: String? = null,
    @SerialName("profile")
    val profile: UserProfileDto? = null,
)