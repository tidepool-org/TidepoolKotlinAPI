package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.dto.AssociationDto
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: Finish implementing automated.v1
@Serializable
@KonvertTo(BasalAutomatedData::class, mapFunctionName = "toDomain")
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
    
    
    @KonvertFrom(BasalAutomatedData::class, mapFunctionName = "fromDomain")
    companion object {}
    
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