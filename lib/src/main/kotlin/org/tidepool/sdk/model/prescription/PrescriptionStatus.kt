package org.tidepool.sdk.model.prescription

import org.tidepool.sdk.dto.prescription.PrescriptionStatusDto

enum class PrescriptionStatus {
    Active,
    Inactive,
    Completed,
    Cancelled,
    Suspended,
}

fun PrescriptionStatusDto.toDomain(): PrescriptionStatus = when (this) {
    PrescriptionStatusDto.Active    -> PrescriptionStatus.Active
    PrescriptionStatusDto.Inactive  -> PrescriptionStatus.Inactive
    PrescriptionStatusDto.Completed -> PrescriptionStatus.Completed
    PrescriptionStatusDto.Cancelled -> PrescriptionStatus.Cancelled
    PrescriptionStatusDto.Suspended -> PrescriptionStatus.Suspended
}

fun PrescriptionStatus.toDto(): PrescriptionStatusDto = when (this) {
    PrescriptionStatus.Active    -> PrescriptionStatusDto.Active
    PrescriptionStatus.Inactive  -> PrescriptionStatusDto.Inactive
    PrescriptionStatus.Completed -> PrescriptionStatusDto.Completed
    PrescriptionStatus.Cancelled -> PrescriptionStatusDto.Cancelled
    PrescriptionStatus.Suspended -> PrescriptionStatusDto.Suspended
}