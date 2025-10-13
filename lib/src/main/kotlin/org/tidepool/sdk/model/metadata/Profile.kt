package org.tidepool.sdk.model.metadata

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.metadata.ProfileDto


data class Profile(
    val fullName: String? = null,
) {
    
    @KonvertFrom(ProfileDto::class, mapFunctionName = "fromDto")
    companion object {}
}
