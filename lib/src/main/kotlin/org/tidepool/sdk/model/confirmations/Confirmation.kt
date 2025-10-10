package org.tidepool.sdk.model.confirmations

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.model.metadata.UserProfile
import org.tidepool.sdk.model.metadata.toDomain
import org.tidepool.sdk.model.metadata.toDto
import java.time.Instant

data class Confirmation(
    val key: String = "",
    val type: ConfirmationType = ConfirmationType.SignupConfirmation,
    val status: ConfirmationStatus = ConfirmationStatus.Pending,
    val email: String = "",
    val creatorId: String = "",
    val created: Instant = Instant.now(),
    val modified: Instant? = null,
    val creator: Creator? = null,
    val context: JsonObject? = null, // no context for context :3
    val restrictions: Restrictions? = null,
    val expiresAt: Instant? = null,
) {
    
    data class Creator(
        val userId: String = "",
        val profile: UserProfile = UserProfile(),
    )
}

internal fun Confirmation.toDto(): ConfirmationDto = ConfirmationDto(
    key = key,
    type = type.toDto(),
    status = status.toDto(),
    email = email,
    creatorId = creatorId,
    created = created,
    modified = modified,
    creator = creator?.toDto(),
    context = context,
    restrictions = restrictions?.toDto(),
    expiresAt = expiresAt
)

internal fun ConfirmationDto.toDomain(): Confirmation = Confirmation(
    key = key,
    type = type.toDomain(),
    status = status.toDomain(),
    email = email,
    creatorId = creatorId,
    created = created,
    modified = modified,
    creator = creator?.toDomain(),
    context = context,
    restrictions = restrictions?.toDomain(),
    expiresAt = expiresAt
)
internal fun Confirmation.Creator.toDto() = ConfirmationDto.CreatorDto(
    userId = userId,
    profile = profile.toDto()
)

internal fun ConfirmationDto.CreatorDto.toDomain() = Confirmation.Creator(
    userId = userId,
    profile = profile.toDomain()
)