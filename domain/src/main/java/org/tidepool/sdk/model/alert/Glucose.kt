package org.tidepool.sdk.model.alert

sealed class Glucose {
    
    abstract val units: String
    
    data class MgDLGlucose(
        override val units: String = "mg/dL",
        val value: Int
    ) : Glucose()
    
    data class MmolGlucose(
        override val units: String = "mmol/L",
        val value: Float
    ) : Glucose()
}