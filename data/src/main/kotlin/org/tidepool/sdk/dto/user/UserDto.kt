package org.tidepool.sdk.dto.user

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.tidepool.sdk.dto.metadata.ProfileDto
import org.tidepool.sdk.model.metadata.users.User
import java.time.Instant

@Serializable
@KonvertTo(User::class, mapFunctionName = "toDomain")
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
    val userName: String? = null,
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
    val profile: ProfileDto? = null,
)
