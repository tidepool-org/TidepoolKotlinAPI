package org.tidepool.sdk.model.confirmations

import io.mcarle.konvert.api.KonvertFrom
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.dto.confirmation.ConfirmationDto
import org.tidepool.sdk.model.metadata.Profile
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
    
    @KonvertFrom(ConfirmationDto::class, mapFunctionName = "fromDto")
    companion object {}
    
    data class Creator(
        val userId: String = "",
        val profile: Profile = Profile(),
    ) {
        @KonvertFrom(ConfirmationDto.CreatorDto::class, mapFunctionName = "fromDto")
        companion object
    }
}