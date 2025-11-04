package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.model.data.BolusData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema bolus.v1
// line 2330
@Serializable
@KonvertTo(BolusData::class, mapFunctionName = "toDomain")
data class BolusDataDto(
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
    
    @SerialName("subType")
    val subType: BolusSubtypeDto = BolusSubtypeDto.Normal,
    @SerialName("deliveryContext")
    val deliveryContext: DeliveryContextDto,
) : BaseDataDto() {
    
    @KonvertFrom(BolusData::class, mapFunctionName = "fromDomain")
    companion object {}
    
    val insulinFormulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}

@Serializable
enum class BolusSubtypeDto {
    @SerialName("automated")
    Automated,
    @SerialName("dual/square")
    DualSquare,
    @SerialName("normal")
    Normal,
    @SerialName("square")
    Square,
    ;
}

@Serializable
enum class DeliveryContextDto {
    
    @SerialName("device")
    Device,
    
    @SerialName("algorithm")
    Algorithm,
    
    @SerialName("remote")
    Remote,
    
    @SerialName("undetermined")
    Undetermined,
    ;
}