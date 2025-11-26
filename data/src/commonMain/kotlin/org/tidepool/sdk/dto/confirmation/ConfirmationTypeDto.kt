package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ConfirmationTypeDto {
    @SerialName("password_reset")
    PasswordReset,
    @SerialName("careteam_invitation")
    CareteamInvitation,
    @SerialName("signup_confirmation")
    SignupConfirmation,
    @SerialName("no_account")
    NoAccount,
    ;
}