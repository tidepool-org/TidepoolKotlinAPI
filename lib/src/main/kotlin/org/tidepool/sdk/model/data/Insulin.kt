package org.tidepool.sdk.model.data

import kotlinx.serialization.Serializable

@Serializable
class Insulin {
    
    @Serializable
    enum class Units {
        Units
    }
}

// schema dose.v1
@Serializable
data class Dose(
    val units: Insulin.Units,
    val total: Double,
    val food: Double?,
    val correction: Double?,
    val active: Double?,
)