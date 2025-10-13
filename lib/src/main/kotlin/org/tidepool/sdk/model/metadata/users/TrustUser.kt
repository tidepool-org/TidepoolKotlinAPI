package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.dto.metadata.users.TrustUserDto
import org.tidepool.sdk.model.metadata.Profile
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
    override val profile: Profile? = null,
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
    
    companion object {
        internal fun fromDto(dto: TrustUserDto): TrustUser = when {
            dto.isTrustor -> TrustorUser(
                permissions = dto.permissions.map { Permission.fromDto(it) }.toSet(),
                emailVerified = dto.emailVerified,
                emails = dto.emails,
                termsAccepted = dto.termsAccepted,
                userId = dto.userId,
                username = dto.userName,
                roles = dto.roles,
                createdTime = dto.createdTime,
                createdUserId = dto.createdUserId,
                modifiedTime = dto.modifiedTime,
                modifiedUserId = dto.modifiedUserId,
                deletedTime = dto.deletedTime,
                deletedUserId = dto.deletedUserId,
                profile = dto.profile?.let {
                    Profile(it.fullName)
                }
            )
            
            dto.isTrustee -> TrusteeUser(
                permissions = dto.permissions.map { Permission.fromDto(it) }.toSet(),
                emailVerified = dto.emailVerified,
                emails = dto.emails,
                termsAccepted = dto.termsAccepted,
                userId = dto.userId,
                username = dto.userName,
                roles = dto.roles,
                createdTime = dto.createdTime,
                createdUserId = dto.createdUserId,
                modifiedTime = dto.modifiedTime,
                modifiedUserId = dto.modifiedUserId,
                deletedTime = dto.deletedTime,
                deletedUserId = dto.deletedUserId,
                profile = dto.profile?.let {
                    Profile(it.fullName)
                }
            )
            
            else -> throw IllegalStateException("TrustUserDto must be either trustor or trustee")
        }
    }
    
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
        override val profile: Profile? = null,
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
        override val profile: Profile? = null,
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
