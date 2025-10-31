package org.tidepool.sdk.dto.prescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.prescription.PrescriptionStatus

@Serializable
enum class PrescriptionStatusDto {
    
    @SerialName("active")
    Active,
    
    @SerialName("inactive")
    Inactive,
    
    @SerialName("completed")
    Completed,
    
    @SerialName("cancelled")
    Cancelled,
    
    @SerialName("suspended")
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