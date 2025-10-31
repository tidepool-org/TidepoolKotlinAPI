package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: Finish implementing automated.v1
@Serializable
data class BasalAutomatedDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>> = emptyList(),
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    override val timeZone: TimeZone? = null,
    @Contextual
    override val timeZoneOffset: Duration? = null,
    
    @SerialName("deliveryType")
    val deliveryType: DeliveryTypeDto,
    @SerialName("duration")
    val duration: Int,
    @SerialName("expectedDuration")
    val expectedDuration: Int? = null,
    @SerialName("rate")
    val rate: Double = -1.0,
    @SerialName("scheduleName")
    val scheduleName: String? = null,
) : BaseDataDto() {
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
    val suppressed: Nothing
        get() = TODO("schema \"scheduled.v1\" not implemented")
    
    @Serializable
    enum class DeliveryTypeDto {
        
        @SerialName("automated")
        Automated,
        
        @SerialName("scheduled")
        Scheduled,
        
        @SerialName("suspend")
        Suspend,
        
        @SerialName("temp")
        Temp,
    }
}

fun BasalAutomatedDataDto.toDomain(): BasalAutomatedData = BasalAutomatedData(
    id = id,
    type = this.type.toDomain(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDomain() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    deliveryType = this.deliveryType.toDomain(),
    duration = duration,
    expectedDuration = expectedDuration,
    rate = rate,
    scheduleName = scheduleName
)

fun BasalAutomatedDataDto.DeliveryTypeDto.toDomain(): BasalAutomatedData.DeliveryType = when (this) {
    BasalAutomatedDataDto.DeliveryTypeDto.Automated -> BasalAutomatedData.DeliveryType.Automated
    BasalAutomatedDataDto.DeliveryTypeDto.Scheduled -> BasalAutomatedData.DeliveryType.Scheduled
    BasalAutomatedDataDto.DeliveryTypeDto.Suspend -> BasalAutomatedData.DeliveryType.Suspend
    BasalAutomatedDataDto.DeliveryTypeDto.Temp -> BasalAutomatedData.DeliveryType.Temp
}

fun BasalAutomatedData.toDto(): BasalAutomatedDataDto = BasalAutomatedDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    deliveryType = this.deliveryType.toDto(),
    duration = duration,
    expectedDuration = expectedDuration,
    rate = rate,
    scheduleName = scheduleName
)

fun BasalAutomatedData.DeliveryType.toDto(): BasalAutomatedDataDto.DeliveryTypeDto = when (this) {
    BasalAutomatedData.DeliveryType.Automated -> BasalAutomatedDataDto.DeliveryTypeDto.Automated
    BasalAutomatedData.DeliveryType.Scheduled -> BasalAutomatedDataDto.DeliveryTypeDto.Scheduled
    BasalAutomatedData.DeliveryType.Suspend -> BasalAutomatedDataDto.DeliveryTypeDto.Suspend
    BasalAutomatedData.DeliveryType.Temp -> BasalAutomatedDataDto.DeliveryTypeDto.Temp
}