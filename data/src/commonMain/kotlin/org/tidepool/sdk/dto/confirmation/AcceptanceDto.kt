package org.tidepool.sdk.dto.confirmation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.confirmations.Acceptance

@Serializable
data class AcceptanceDto(
    @SerialName("password")
    val password: String,
    @SerialName("birthday")
    val birthday: String, // format: "2012-08-30"
)

fun AcceptanceDto.toDomain(): Acceptance = Acceptance(
    password = password,
    birthday = birthday
)