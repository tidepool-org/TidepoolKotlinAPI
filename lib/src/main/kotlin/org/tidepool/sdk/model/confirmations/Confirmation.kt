package org.tidepool.sdk.model.confirmations

import org.tidepool.sdk.model.metadata.Profile
import java.time.Instant

data class Confirmation(
        val key: String,
        val type: ConfirmationType,
        val status: ConfirmationStatus,
        val email: String,
        val creatorId: String,
        val created: Instant,
    val modified: Instant?,
    val creator: Creator?,
    val context: String?, // no context for context :3
    val restrictions: Restrictions?,
    val expiresAt: Instant?,
) {
    data class Creator(
        val userId: String,
        val profile: Profile,
    )
}
