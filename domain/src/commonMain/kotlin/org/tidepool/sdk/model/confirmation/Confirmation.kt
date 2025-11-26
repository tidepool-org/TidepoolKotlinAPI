package org.tidepool.sdk.model.confirmation

import kotlinx.datetime.Clock
import org.tidepool.sdk.model.metadata.UserProfile
import kotlinx.datetime.Instant

data class Confirmation(
    val key: String = "",
    val type: ConfirmationType = ConfirmationType.SignupConfirmation,
    val status: ConfirmationStatus = ConfirmationStatus.Pending,
    val email: String = "",
    val creatorId: String = "",
    val created: Instant = Clock.System.now(),
    val modified: Instant? = null,
    val creator: Creator? = null,
    val context: Map<String, Any>? = null, // no context for context :3
    val restrictions: Restrictions? = null,
    val expiresAt: Instant? = null,
) {
    
    data class Creator(
        val userId: String = "",
        val profile: UserProfile = UserProfile(),
    )
}