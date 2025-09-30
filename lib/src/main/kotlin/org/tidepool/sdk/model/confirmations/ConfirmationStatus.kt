package org.tidepool.sdk.model.confirmations

import org.tidepool.sdk.dto.confirmation.ConfirmationStatusDto
import org.tidepool.sdk.model.confirmations.ConfirmationStatus.Canceled
import org.tidepool.sdk.model.confirmations.ConfirmationStatus.Completed
import org.tidepool.sdk.model.confirmations.ConfirmationStatus.Declined
import org.tidepool.sdk.model.confirmations.ConfirmationStatus.Pending

enum class ConfirmationStatus {
    Pending,
    Completed,
    Canceled,
    Declined,
    ;
}

internal fun ConfirmationStatus.toDto() = when (this) {
    Pending   -> ConfirmationStatusDto.Pending
    Completed -> ConfirmationStatusDto.Completed
    Canceled -> ConfirmationStatusDto.Canceled
    Declined -> ConfirmationStatusDto.Declined
}

internal fun ConfirmationStatusDto.toDomain() = when (this) {
    ConfirmationStatusDto.Pending   -> Pending
    ConfirmationStatusDto.Completed -> Completed
    ConfirmationStatusDto.Canceled -> Canceled
    ConfirmationStatusDto.Declined -> Declined
}