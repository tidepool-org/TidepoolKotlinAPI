package org.tidepool.sdk.model.confirmations

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.model.metadata.Profile
import java.time.Instant

@Serializable
data class Confirmation(
    val key: String = "",
    val type: ConfirmationType = ConfirmationType.signup_confirmation,
    val status: ConfirmationStatus = ConfirmationStatus.pending,
    val email: String = "",
    val creatorId: String = "",
    @Contextual val created: Instant = Instant.now(),
    @Contextual val modified: Instant? = null,
    val creator: Creator? = null,
    val context: JsonObject? = null, // no context for context :3
    val restrictions: Restrictions? = null,
    @Contextual val expiresAt: Instant? = null,
) {
    
    @Serializable
    data class Creator(
        val userId: String = "",
        val profile: Profile = Profile(),
    )
}
