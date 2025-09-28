package org.tidepool.sdk.model.confirmations

import kotlinx.serialization.Serializable

@Serializable
enum class ConfirmationType {
    password_reset,
    careteam_invitation,
    signup_confirmation,
    no_account
}