package org.tidepool.sdk.model.auth

import java.time.Instant

data class User(
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
	val deletedUserId: String? = null
)