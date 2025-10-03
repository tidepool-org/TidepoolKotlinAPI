package org.tidepool.sdk.model.confirmations

import kotlinx.serialization.Serializable

@Serializable
enum class ConfirmationStatus {
    pending,
    completed,
    canceled,
    declined
}