package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.metadata.ProfileDto
import java.time.Instant

@Serializable
data class ConfirmationDto(
    val key: String = "",
    val type: ConfirmationTypeDto = ConfirmationTypeDto.SignupConfirmation,
    val status: ConfirmationStatusDto = ConfirmationStatusDto.Pending,
    val email: String = "",
    val creatorId: String = "",
    @Contextual val created: Instant = Instant.now(),
    @Contextual val modified: Instant? = null,
    val creator: CreatorDto? = null,
    val context: JsonObject? = null, // no context for context :3
    val restrictions: RestrictionsDto? = null,
    @Contextual val expiresAt: Instant? = null,
) {
    
    @Serializable
    data class CreatorDto(
        val userId: String = "",
        val profile: ProfileDto = ProfileDto(),
    )
}
