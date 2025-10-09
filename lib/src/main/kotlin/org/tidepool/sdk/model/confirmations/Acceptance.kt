package org.tidepool.sdk.model.confirmations

import org.tidepool.sdk.dto.confirmation.AcceptanceDto

data class Acceptance(
    val password: String,
    val birthday: String, // format: "2012-08-30"
)

internal fun Acceptance.toDto(): AcceptanceDto = AcceptanceDto(
    password = password,
    birthday = birthday,
)
