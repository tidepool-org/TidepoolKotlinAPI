package org.tidepool.sdk.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.deserialization.ResultType
import org.tidepool.sdk.model.Association
import java.time.Instant
import java.util.TimeZone
import kotlin.reflect.KClass
import kotlin.time.Duration

// TODO: finish implementing base.v1
@Serializable
sealed class BaseData(
    val type: DataType = DataType.Alert,
    @Contextual val time: Instant? = null,
    val annotations: Array<Map<String, String>>? = null,
    val associations: Array<Association>? = null,
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
    enum class DataType(override val subclassType: KClass<out BaseData>) : ResultType<BaseData> {
        
        @SerialName("alert")
        Alert(BaseData::class),
        
        @SerialName("basal")
        Basal(BasalAutomatedData::class),
        
        @SerialName("bloodKetone")
        BloodKetone(BaseData::class),
        
        @SerialName("bolus")
        Bolus(BolusData::class),
        
        @SerialName("wizard")
        Calculator(BaseData::class),
        
        @SerialName("cbg")
        Cbg(ContinuousGlucoseData::class),
        
        @SerialName("cgmSettings")
        CgmSettings(BaseData::class),
        
        @SerialName("controllerSettings")
        ControllerSettings(BaseData::class),
        
        @SerialName("controllerStatus")
        ControllerStatus(BaseData::class),
        
        @SerialName("deviceEvent")
        DeviceEvent(BaseData::class),
        
        @SerialName("dosingDecision")
        DosingDecision(DosingDecisionData::class),
        
        @SerialName("food")
        Food(FoodData::class),
        
        @SerialName("insulin")
        Insulin(InsulinData::class),
        
        @SerialName("physicalActivity")
        PhysicalActivity(BaseData::class),
        
        @SerialName("pumpSettings")
        PumpSettings(BaseData::class),
        
        @SerialName("pumpStatus")
        PumpStatus(BaseData::class),
        
        @SerialName("reportedState")
        ReportedState(BaseData::class),
        
        @SerialName("smbg")
        Smbg(BaseData::class)
    }
}