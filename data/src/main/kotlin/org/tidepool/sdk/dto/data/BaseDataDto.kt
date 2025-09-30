package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.deserialization.ResultType
import org.tidepool.sdk.dto.AssociationDto
import java.time.Instant
import java.util.TimeZone
import kotlin.reflect.KClass
import kotlin.time.Duration

// TODO: finish implementing base.v1
@Serializable
sealed class BaseDataDto(
    val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual val time: Instant? = null,
    val annotations: Array<Map<String, String>>? = null,
    val associations: Array<AssociationDto>? = null,
    @Contextual val clockDriftOffset: Duration? = null,
    @Contextual val conversionOffset: Duration? = null,
    val dataSetId: String? = null,
    val deviceTime: String? = null,
    val id: String? = null,
    val notes: Array<String>? = null,
    @Contextual val timeZone: TimeZone? = null,
    @Contextual val timeZoneOffset: Duration? = null
) {
    
    val location: Nothing
        get() = TODO("schema \"\" not implemented")
    
    @Serializable
    enum class DataTypeDto(override val subclassType: KClass<out BaseDataDto>) :
        ResultType<BaseDataDto> {
        
        @SerialName("alert")
        Alert(BaseDataDto::class),
        
        @SerialName("basal")
        Basal(BasalAutomatedDataDto::class),
        
        @SerialName("bloodKetone")
        BloodKetone(BaseDataDto::class),
        
        @SerialName("bolus")
        Bolus(BolusDataDto::class),
        
        @SerialName("wizard")
        Calculator(BaseDataDto::class),
        
        @SerialName("cbg")
        Cbg(ContinuousGlucoseDataDto::class),
        
        @SerialName("cgmSettings")
        CgmSettings(BaseDataDto::class),
        
        @SerialName("controllerSettings")
        ControllerSettings(BaseDataDto::class),
        
        @SerialName("controllerStatus")
        ControllerStatus(BaseDataDto::class),
        
        @SerialName("deviceEvent")
        DeviceEvent(BaseDataDto::class),
        
        @SerialName("dosingDecision")
        DosingDecision(DosingDecisionDataDto::class),
        
        @SerialName("food")
        Food(FoodDataDto::class),
        
        @SerialName("insulin")
        Insulin(InsulinDataDto::class),
        
        @SerialName("physicalActivity")
        PhysicalActivity(BaseDataDto::class),
        
        @SerialName("pumpSettings")
        PumpSettings(BaseDataDto::class),
        
        @SerialName("pumpStatus")
        PumpStatus(BaseDataDto::class),
        
        @SerialName("reportedState")
        ReportedState(BaseDataDto::class),
        
        @SerialName("smbg")
        Smbg(BaseDataDto::class)
    }
}