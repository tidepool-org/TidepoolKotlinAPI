package org.tidepool.sdk.model.data

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.dto.data.DoseDto
import org.tidepool.sdk.dto.data.InsulinDto

class Insulin {
    
    enum class Units {
        
        Units
    }
}

data class Dose(
    val units: Insulin.Units,
    val total: Double,
    val food: Double?,
    val correction: Double?,
    val active: Double?,
) {
    
    @KonvertFrom(DoseDto::class, mapFunctionName = "fromDto")
    companion object {}
}