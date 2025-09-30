package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ConfirmationStatusDto {
    @SerialName("pending")
    Pending,
    @SerialName("completed")
    Completed,
    @SerialName("canceled")
    Canceled,
    @SerialName("declined")
    Declined
}