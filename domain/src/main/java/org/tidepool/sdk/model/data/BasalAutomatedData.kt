package org.tidepool.sdk.model.data

data class BasalAutomatedData(
    val deliveryType: DeliveryType,
    val duration: Int,
    val expectedDuration: Int? = null,
    val rate: Double = -1.0,
    val scheduleName: String? = null,
) : BaseData(DataType.Basal) {
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
    val suppressed: Nothing
        get() = TODO("schema \"scheduled.v1\" not implemented")
    
    enum class DeliveryType {
        Automated,
        Scheduled,
        Suspend,
        Temp,
    }
}