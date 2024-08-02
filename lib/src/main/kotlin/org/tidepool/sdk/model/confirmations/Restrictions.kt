package org.tidepool.sdk.model.confirmations

data class Restrictions(
    val canAccept: Boolean,
    val requiredIdp: String?
)