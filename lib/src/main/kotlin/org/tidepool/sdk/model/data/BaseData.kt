package org.tidepool.sdk.model.data

import io.mcarle.konvert.api.KonvertFrom
import org.tidepool.sdk.deserialization.ResultType
import org.tidepool.sdk.dto.data.*
import org.tidepool.sdk.model.Association
import java.time.Instant
import java.util.TimeZone
import kotlin.reflect.KClass
import kotlin.time.Duration

// TODO: finish implementing base.v1
sealed class BaseData(
    val type: DataType = DataType.Alert,
    val time: Instant? = null,
    val annotations: Array<Map<String, String>>? = null,
    val associations: Array<Association>? = null,
    val clockDriftOffset: Duration? = null,
    val conversionOffset: Duration? = null,
    val dataSetId: String? = null,
    val deviceTime: String? = null,
    val id: String? = null,
    val notes: Array<String>? = null,
    val timeZone: TimeZone? = null,
    val timeZoneOffset: Duration? = null
) {
    
    val location: Nothing
        get() = TODO("schema \"\" not implemented")
    
    enum class DataType() {
        Alert,
        Basal,
        BloodKetone,
        Bolus,
        Calculator,
        Cbg,
        CgmSettings,
        ControllerSettings,
        ControllerStatus,
        DeviceEvent,
        DosingDecision,
        Food,
        Insulin,
        PhysicalActivity,
        PumpSettings,
        PumpStatus,
        ReportedState,
        Smbg
    }
    
    companion object {
        
        internal fun fromDto(dto: BaseDataDto) = when (dto) {
            is BasalAutomatedDataDto    -> BasalAutomatedData.fromDto(dto)
            is BolusDataDto             -> BolusData.fromDto(dto)
            is ContinuousGlucoseDataDto -> ContinuousGlucoseData.fromDto(dto)
            is DosingDecisionDataDto    -> DosingDecisionData.fromDto(dto)
            is FoodDataDto              -> FoodData.fromDto(dto)
            is InsulinDataDto           -> InsulinData.fromDto(dto)
        }
    }
}