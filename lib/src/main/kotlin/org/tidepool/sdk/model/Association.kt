package org.tidepool.sdk.model

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.AssociationDto

public data class Association(
    val type: AssociationType?,
    val id: String?,
    val url: String?,
    val reason: String?
) {
    
    @KonvertFrom(AssociationDto::class, mapFunctionName = "fromDto")
    companion object {}
    
    enum class AssociationType {
        Blob,
        Datum,
        Image,
        Url,
        ;
    }
}