package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.confirmation.ConfirmationStatus

@Serializable
enum class ConfirmationStatusDto {
    
    @SerialName("pending")
    Pending,
    
    @SerialName("completed")
    Completed,
    
    @SerialName("canceled")
    Canceled,
    
    @SerialName("declined")
    Declined,
    ;
}

fun ConfirmationStatusDto.toDomain(): ConfirmationStatus = when (this) {
    ConfirmationStatusDto.Pending   -> ConfirmationStatus.Pending
    ConfirmationStatusDto.Completed -> ConfirmationStatus.Completed
    ConfirmationStatusDto.Canceled  -> ConfirmationStatus.Canceled
    ConfirmationStatusDto.Declined  -> ConfirmationStatus.Declined
}