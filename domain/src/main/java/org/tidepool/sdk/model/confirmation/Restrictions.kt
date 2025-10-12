package org.tidepool.sdk.model.confirmation

data class Restrictions(
    val canAccept: Boolean,
    val requiredIdp: String?
)