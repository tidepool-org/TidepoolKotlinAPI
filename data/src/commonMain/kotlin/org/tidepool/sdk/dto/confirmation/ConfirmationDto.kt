package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.tidepool.sdk.model.confirmation.Confirmation
import org.tidepool.sdk.dto.metadata.UserProfileDto
import org.tidepool.sdk.model.metadata.UserProfile
import java.time.Instant

@Serializable
data class ConfirmationDto(
    @SerialName("key")
    val key: String = "",
    @SerialName("type")
    val type: ConfirmationTypeDto = ConfirmationTypeDto.SignupConfirmation,
    @SerialName("status")
    val status: ConfirmationStatusDto = ConfirmationStatusDto.Pending,
    @SerialName("email")
    val email: String = "",
    @SerialName("creatorId")
    val creatorId: String = "",
    @Contextual
    @SerialName("created")
    val created: Instant = Instant.now(),
    @Contextual
    @SerialName("modified")
    val modified: Instant? = null,
    @SerialName("creator")
    val creator: CreatorDto? = null,
    @SerialName("context")
    val context: JsonObject? = null, // no context for context :3
    @SerialName("restrictions")
    val restrictions: RestrictionsDto? = null,
    @Contextual
    @SerialName("expiresAt")
    val expiresAt: Instant? = null,
) {
    
    @Serializable
    data class CreatorDto(
        @SerialName("userId")
        val userId: String = "",
        @SerialName("profile")
        val profile: UserProfileDto = UserProfileDto(),
    )
}

// Manual mapping functions
internal fun ConfirmationDto.toDomain(): Confirmation = Confirmation(
    key = key,
    type = type.toDomain(),
    status = status.toDomain(),
    email = email,
    creatorId = creatorId,
    created = created,
    modified = modified,
    creator = creator?.toDomain(),
    context = context?.let { jsonObject ->
        jsonObject.entries.associate { (key, value) ->
            key to when (value) {
                is JsonPrimitive -> value.content
                else             -> value.toString()
            }
        }
    },
    restrictions = null,
    expiresAt = expiresAt
)

internal fun ConfirmationDto.CreatorDto.toDomain(): Confirmation.Creator = Confirmation.Creator(
    userId = userId,
    profile = UserProfile(fullName = profile.fullName)
)

internal fun ConfirmationTypeDto.toDomain(): org.tidepool.sdk.model.confirmation.ConfirmationType =
    when (this) {
        ConfirmationTypeDto.PasswordReset      -> org.tidepool.sdk.model.confirmation.ConfirmationType.PasswordReset
        ConfirmationTypeDto.CareteamInvitation -> org.tidepool.sdk.model.confirmation.ConfirmationType.CareteamInvitation
        ConfirmationTypeDto.SignupConfirmation -> org.tidepool.sdk.model.confirmation.ConfirmationType.SignupConfirmation
        ConfirmationTypeDto.NoAccount          -> org.tidepool.sdk.model.confirmation.ConfirmationType.NoAccount
    }
