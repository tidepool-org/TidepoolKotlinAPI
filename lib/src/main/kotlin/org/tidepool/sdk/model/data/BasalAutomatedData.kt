package org.tidepool.sdk.model.data

// TODO: Finish implementing automated.v1
data class BasalAutomatedData(
    val deliveryType: DeliveryType,
    val duration: Int,
    val expectedDuration: Int? = null,
    val rate: Double = -1.0,
    val scheduleName: String? = null,
) : BaseData(DataType.basal) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
    val suppressed: Nothing
        get() = TODO("schema \"scheduled.v1\" not implemented")
    
    enum class DeliveryType {
        automated,
        scheduled,
        suspend,
        temp
    }
}