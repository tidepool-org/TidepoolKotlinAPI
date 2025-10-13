package org.tidepool.sdk.model.data

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
)