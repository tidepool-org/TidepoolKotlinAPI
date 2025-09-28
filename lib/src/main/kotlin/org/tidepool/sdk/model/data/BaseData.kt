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
    val type: DataType = DataType.alert,
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
        alert(BaseData::class),
        basal(BasalAutomatedData::class),
        bloodKetone(BaseData::class),
        bolus(BolusData::class),
        
        @SerialName("wizard")
        calculator(BaseData::class),
        cbg(ContinuousGlucoseData::class),
        cgmSettings(BaseData::class),
        controllerSettings(BaseData::class),
        controllerStatus(BaseData::class),
        deviceEvent(BaseData::class),
        dosingDecision(DosingDecisionData::class),
        food(FoodData::class),
        insulin(InsulinData::class),
        physicalActivity(BaseData::class),
        pumpSettings(BaseData::class),
        pumpStatus(BaseData::class),
        reportedState(BaseData::class),
        smbg(BaseData::class)
    }
}