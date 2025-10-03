package org.tidepool.sdk.model.confirmations

import kotlinx.serialization.Serializable

@Serializable
data class Restrictions(
    val canAccept: Boolean,
    val requiredIdp: String?
)