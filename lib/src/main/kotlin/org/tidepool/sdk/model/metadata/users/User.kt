package org.tidepool.sdk.model.metadata.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.model.metadata.Profile
import java.time.Instant

@Serializable
open class User(
    val emailVerified: Boolean = false,
    val emails: List<String>? = null,
    @Contextual val termsAccepted: Instant? = null,
    val userid: String = "",
    val username: String? = null,
    val roles: List<String>? = null,
    @Contextual val createdTime: Instant? = null,
    val createdUserId: String? = null,
    @Contextual val modifiedTime: Instant? = null,
    val modifiedUserId: String? = null,
    @Contextual val deletedTime: Instant? = null,
    val deletedUserId: String? = null,
    val profile: Profile? = null,
)