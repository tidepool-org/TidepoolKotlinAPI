package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.ProfileDto
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
        val profile: ProfileDto = ProfileDto(),
    )
}
