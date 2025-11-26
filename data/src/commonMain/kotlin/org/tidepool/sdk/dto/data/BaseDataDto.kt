package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.deserialization.ResultType
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.BolusData
import org.tidepool.sdk.model.data.CgmSettingsData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import org.tidepool.sdk.model.data.ControllerSettingsData
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.DeviceEventData
import org.tidepool.sdk.model.data.DosingDecisionData
import org.tidepool.sdk.model.data.FoodData
import org.tidepool.sdk.model.data.InsulinData
import org.tidepool.sdk.model.data.PumpSettingsData
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.reflect.KClass
import kotlin.time.Duration

// TODO: finish implementing base.v1
@Serializable
abstract class BaseDataDto {
    @SerialName("id")
    abstract val id: String
    @SerialName("type")
    abstract val type: DataTypeDto
    @SerialName("time")
    abstract val time: Instant?
    @SerialName("annotations")
    abstract val annotations: List<Map<String, String>>
    @SerialName("associations")
    abstract val associations: List<AssociationDto>
    @SerialName("clockDriftOffset")
    abstract val clockDriftOffset: Duration?
    @SerialName("conversionOffset")
    abstract val conversionOffset: Duration?
    @SerialName("dataSetId")
    abstract val dataSetId: String?
    @SerialName("deviceTime")
    abstract val deviceTime: String?
    @SerialName("notes")
    abstract val notes: List<String>
    @Contextual
    @SerialName("timeZone")
    abstract val timeZone: TimeZone?
    @SerialName("timeZoneOffset")
    abstract val timeZoneOffset: Int?

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
        CgmSettings(CgmSettingsDataDto::class),

        @SerialName("controllerSettings")
        ControllerSettings(ControllerSettingsDataDto::class),

        @SerialName("controllerStatus")
        ControllerStatus(BaseDataDto::class),

        @SerialName("deviceEvent")
        DeviceEvent(DeviceEventDataDto::class),

        @SerialName("dosingDecision")
        DosingDecision(DosingDecisionDataDto::class),

        @SerialName("food")
        Food(FoodDataDto::class),

        @SerialName("insulin")
        Insulin(InsulinDataDto::class),

        @SerialName("physicalActivity")
        PhysicalActivity(BaseDataDto::class),

        @SerialName("pumpSettings")
        PumpSettings(PumpSettingsDataDto::class),

        @SerialName("pumpStatus")
        PumpStatus(BaseDataDto::class),

        @SerialName("reportedState")
        ReportedState(BaseDataDto::class),

        @SerialName("smbg")
        Smbg(BaseDataDto::class)
    }
}

internal fun BaseDataDto.toDomain(): BaseData = when (this) {
    is BasalAutomatedDataDto        -> toDomain()
    is BolusDataDto                 -> toDomain()
    is ContinuousGlucoseDataDto     -> toDomain()
    is DosingDecisionDataDto        -> toDomain()
    is FoodDataDto                  -> toDomain()
    is InsulinDataDto               -> toDomain()
    is CgmSettingsDataDto           -> toDomain()
    is ControllerSettingsDataDto    -> toDomain()
    is PumpSettingsDataDto          -> toDomain()
    is DeviceEventDataDto           -> toDomain()
    else -> throw IllegalArgumentException("Unknown BaseDataDto type: ${this::class.simpleName}")
}

fun BaseDataDto.DataTypeDto.toDomain(): DataType = when (this) {
    BaseDataDto.DataTypeDto.Alert ->                DataType.Alert
    BaseDataDto.DataTypeDto.Basal ->                DataType.Basal
    BaseDataDto.DataTypeDto.BloodKetone ->          DataType.BloodKetone
    BaseDataDto.DataTypeDto.Bolus ->                DataType.Bolus
    BaseDataDto.DataTypeDto.Calculator ->           DataType.Calculator
    BaseDataDto.DataTypeDto.Cbg ->                  DataType.Cbg
    BaseDataDto.DataTypeDto.CgmSettings ->          DataType.CgmSettings
    BaseDataDto.DataTypeDto.ControllerSettings ->   DataType.ControllerSettings
    BaseDataDto.DataTypeDto.ControllerStatus ->     DataType.ControllerStatus
    BaseDataDto.DataTypeDto.DeviceEvent ->          DataType.DeviceEvent
    BaseDataDto.DataTypeDto.DosingDecision ->       DataType.DosingDecision
    BaseDataDto.DataTypeDto.Food ->                 DataType.Food
    BaseDataDto.DataTypeDto.Insulin ->              DataType.Insulin
    BaseDataDto.DataTypeDto.PhysicalActivity ->     DataType.PhysicalActivity
    BaseDataDto.DataTypeDto.PumpSettings ->         DataType.PumpSettings
    BaseDataDto.DataTypeDto.PumpStatus ->           DataType.PumpStatus
    BaseDataDto.DataTypeDto.ReportedState ->        DataType.ReportedState
    BaseDataDto.DataTypeDto.Smbg ->                 DataType.Smbg
}

internal fun BaseData.toDto(): BaseDataDto = when (this) {
    is BasalAutomatedData       -> toDto()
    is BolusData                -> toDto()
    is ContinuousGlucoseData    -> toDto()
    is DosingDecisionData       -> toDto()
    is FoodData                 -> toDto()
    is InsulinData              -> toDto()
    is CgmSettingsData          -> toDto()
    is ControllerSettingsData   -> toDto()
    is PumpSettingsData         -> toDto()
    is DeviceEventData          -> toDto()
}

internal fun DataType.toDto(): BaseDataDto.DataTypeDto = when (this) {
    DataType.Alert              -> BaseDataDto.DataTypeDto.Alert
    DataType.Basal              -> BaseDataDto.DataTypeDto.Basal
    DataType.BloodKetone        -> BaseDataDto.DataTypeDto.BloodKetone
    DataType.Bolus              -> BaseDataDto.DataTypeDto.Bolus
    DataType.Calculator         -> BaseDataDto.DataTypeDto.Calculator
    DataType.Cbg                -> BaseDataDto.DataTypeDto.Cbg
    DataType.CgmSettings        -> BaseDataDto.DataTypeDto.CgmSettings
    DataType.ControllerSettings -> BaseDataDto.DataTypeDto.ControllerSettings
    DataType.ControllerStatus   -> BaseDataDto.DataTypeDto.ControllerStatus
    DataType.DeviceEvent        -> BaseDataDto.DataTypeDto.DeviceEvent
    DataType.DosingDecision     -> BaseDataDto.DataTypeDto.DosingDecision
    DataType.Food               -> BaseDataDto.DataTypeDto.Food
    DataType.Insulin            -> BaseDataDto.DataTypeDto.Insulin
    DataType.PhysicalActivity   -> BaseDataDto.DataTypeDto.PhysicalActivity
    DataType.PumpSettings       -> BaseDataDto.DataTypeDto.PumpSettings
    DataType.PumpStatus         -> BaseDataDto.DataTypeDto.PumpStatus
    DataType.ReportedState      -> BaseDataDto.DataTypeDto.ReportedState
    DataType.Smbg               -> BaseDataDto.DataTypeDto.Smbg
}