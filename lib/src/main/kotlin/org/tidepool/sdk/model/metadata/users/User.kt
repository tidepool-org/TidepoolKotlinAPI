package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.model.metadata.Profile
import java.time.Instant

open class User(
    val emailVerified: Boolean = false,
    val emails: List<String>? = null,
    val termsAccepted: Instant? = null,
    val userid: String = "",
    val username: String? = null,
    val roles: List<String>? = null,
    val createdTime: Instant? = null,
    val createdUserId: String? = null,
    val modifiedTime: Instant? = null,
    val modifiedUserId: String? = null,
    val deletedTime: Instant? = null,
    val deletedUserId: String? = null,
    val profile: Profile? = null,
)