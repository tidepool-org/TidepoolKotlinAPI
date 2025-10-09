package org.tidepool.sdk.model.confirmations

import org.tidepool.sdk.dto.confirmation.ConfirmationUpsertDto

data class ConfirmationUpsert(
    val clinicId: String? = null,
    val invitedBy: String? = null,
)

internal fun ConfirmationUpsert.toDto(): ConfirmationUpsertDto = ConfirmationUpsertDto(
    clinicId = clinicId,
    invitedBy = invitedBy,
)

internal fun ConfirmationUpsertDto.toDomain(): ConfirmationUpsert = ConfirmationUpsert(
    clinicId = clinicId,
    invitedBy = invitedBy,
)
