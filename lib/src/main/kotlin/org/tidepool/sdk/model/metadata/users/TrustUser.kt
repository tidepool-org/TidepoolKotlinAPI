package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import org.tidepool.sdk.model.metadata.UserProfile
import org.tidepool.sdk.model.metadata.toDomain
import java.time.Instant
import kotlin.collections.map

sealed class TrustUser(
    override val emailVerified: Boolean = false,
    override val emails: List<String>? = null,
    override val termsAccepted: Instant? = null,
    override val userId: String = "",
    override val username: String? = null,
    override val roles: List<String>? = null,
    override val createdTime: Instant? = null,
    override val createdUserId: String? = null,
    override val modifiedTime: Instant? = null,
    override val modifiedUserId: String? = null,
    override val deletedTime: Instant? = null,
    override val deletedUserId: String? = null,
    override val profile: UserProfile? = null,
) : User(
    emailVerified = emailVerified,
    emails = emails,
    termsAccepted = termsAccepted,
    userId = userId,
    username = username,
    roles = roles,
    createdTime = createdTime,
    createdUserId = createdUserId,
    modifiedTime = modifiedTime,
    modifiedUserId = modifiedUserId,
    deletedTime = deletedTime,
    deletedUserId = deletedUserId,
    profile = profile,
) {
    
    data class TrustorUser(
        val permissions: Set<Permission>,
        override val emailVerified: Boolean = false,
        override val emails: List<String>? = null,
        override val termsAccepted: Instant? = null,
        override val userId: String = "",
        override val username: String? = null,
        override val roles: List<String>? = null,
        override val createdTime: Instant? = null,
        override val createdUserId: String? = null,
        override val modifiedTime: Instant? = null,
        override val modifiedUserId: String? = null,
        override val deletedTime: Instant? = null,
        override val deletedUserId: String? = null,
        override val profile: UserProfile? = null,
    ) : TrustUser(
        emailVerified = emailVerified,
        emails = emails,
        termsAccepted = termsAccepted,
        userId = userId,
        username = username,
        roles = roles,
        createdTime = createdTime,
        createdUserId = createdUserId,
        modifiedTime = modifiedTime,
        modifiedUserId = modifiedUserId,
        deletedTime = deletedTime,
        deletedUserId = deletedUserId,
        profile = profile,
    )
    
    data class TrusteeUser(
        val permissions: Set<Permission>,
        override val emailVerified: Boolean = false,
        override val emails: List<String>? = null,
        override val termsAccepted: Instant? = null,
        override val userId: String = "",
        override val username: String? = null,
        override val roles: List<String>? = null,
        override val createdTime: Instant? = null,
        override val createdUserId: String? = null,
        override val modifiedTime: Instant? = null,
        override val modifiedUserId: String? = null,
        override val deletedTime: Instant? = null,
        override val deletedUserId: String? = null,
        override val profile: UserProfile? = null,
    ) : TrustUser(
        emailVerified = emailVerified,
        emails = emails,
        termsAccepted = termsAccepted,
        userId = userId,
        username = username,
        roles = roles,
        createdTime = createdTime,
        createdUserId = createdUserId,
        modifiedTime = modifiedTime,
        modifiedUserId = modifiedUserId,
        deletedTime = deletedTime,
        deletedUserId = deletedUserId,
        profile = profile,
    )
}

internal fun TrustUserDto.toDomain(): TrustUser = when {
    isTrustor -> TrustUser.TrustorUser(
        permissions = permissions.map { it.toDomain() }.toSet(),
        emailVerified = emailVerified,
        emails = emails,
        termsAccepted = termsAccepted,
        userId = userId,
        username = userName,
        roles = roles,
        createdTime = createdTime,
        createdUserId = createdUserId,
        modifiedTime = modifiedTime,
        modifiedUserId = modifiedUserId,
        deletedTime = deletedTime,
        deletedUserId = deletedUserId,
        profile = profile?.toDomain()
    )
    
    isTrustee -> TrustUser.TrusteeUser(
        permissions = permissions.map { it.toDomain() }.toSet(),
        emailVerified = emailVerified,
        emails = emails,
        termsAccepted = termsAccepted,
        userId = userId,
        username = userName,
        roles = roles,
        createdTime = createdTime,
        createdUserId = createdUserId,
        modifiedTime = modifiedTime,
        modifiedUserId = modifiedUserId,
        deletedTime = deletedTime,
        deletedUserId = deletedUserId,
        profile = profile?.toDomain()
    )
    
    else      -> throw IllegalStateException("TrustUserDto must be either trustor or trustee")
}
