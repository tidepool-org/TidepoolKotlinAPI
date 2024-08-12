package org.tidepool.sdk.model.confirmations

import com.google.gson.JsonObject
import org.tidepool.sdk.model.metadata.Profile
import java.time.Instant

data class Confirmation(
	val key: String = "",
	val type: ConfirmationType = ConfirmationType.signup_confirmation,
	val status: ConfirmationStatus = ConfirmationStatus.pending,
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
        val profile: Profile = Profile(),
    )
}
