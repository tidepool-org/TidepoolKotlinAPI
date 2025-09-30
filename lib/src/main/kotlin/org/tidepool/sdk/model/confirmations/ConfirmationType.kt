package org.tidepool.sdk.model.confirmations

import org.tidepool.sdk.dto.confirmation.ConfirmationTypeDto

enum class ConfirmationType {
    
    PasswordReset,
    CareteamInvitation,
    SignupConfirmation,
    NoAccount,
    ;
}

internal fun ConfirmationType.toDto() = when (this) {
    ConfirmationType.PasswordReset      -> ConfirmationTypeDto.PasswordReset
    ConfirmationType.CareteamInvitation -> ConfirmationTypeDto.CareteamInvitation
    ConfirmationType.SignupConfirmation -> ConfirmationTypeDto.SignupConfirmation
    ConfirmationType.NoAccount          -> ConfirmationTypeDto.NoAccount
}

internal fun ConfirmationTypeDto.toDomain() = when (this) {
    ConfirmationTypeDto.PasswordReset      -> ConfirmationType.PasswordReset
    ConfirmationTypeDto.CareteamInvitation -> ConfirmationType.CareteamInvitation
    ConfirmationTypeDto.SignupConfirmation -> ConfirmationType.SignupConfirmation
    ConfirmationTypeDto.NoAccount          -> ConfirmationType.NoAccount
}