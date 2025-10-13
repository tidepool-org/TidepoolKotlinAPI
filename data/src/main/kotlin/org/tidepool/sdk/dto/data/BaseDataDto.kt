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
    @SerialName("type")
    val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual
    @SerialName("time")
    val time: Instant? = null,
    @SerialName("annotations")
    val annotations: Array<Map<String, String>>? = null,
    @SerialName("associations")
    val associations: Array<AssociationDto>? = null,
    @Contextual
    @SerialName("clockDriftOffset")
    val clockDriftOffset: Duration? = null,
    @Contextual
    @SerialName("conversionOffset")
    val conversionOffset: Duration? = null,
    @SerialName("dataSetId")
    val dataSetId: String? = null,
    @SerialName("deviceTime")
    val deviceTime: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("notes")
    val notes: Array<String>? = null,
    @Contextual
    @SerialName("timeZone")
    val timeZone: TimeZone? = null,
    @Contextual
    @SerialName("timeZoneOffset")
    val timeZoneOffset: Duration? = null
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