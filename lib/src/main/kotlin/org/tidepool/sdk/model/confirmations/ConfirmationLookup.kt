package org.tidepool.sdk.model.confirmations

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.confirmation.ConfirmationLookupDto

data class ConfirmationLookup(
    val key: String,
) {
    
    @KonvertFrom(ConfirmationLookupDto::class, mapFunctionName = "fromDto")
    companion object {}
}