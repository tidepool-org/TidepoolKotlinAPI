package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.FoodDataDto
import org.tidepool.sdk.dto.data.InsulinDataDto
import org.tidepool.sdk.model.Association
import java.time.Instant
import java.util.TimeZone
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
}

internal fun BaseDataDto.toDomain() = when (this) {
    is BasalAutomatedDataDto    -> toDomain()
    is BolusDataDto             -> toDomain()
    is ContinuousGlucoseDataDto -> toDomain()
    is DosingDecisionDataDto    -> toDomain()
    is FoodDataDto              -> toDomain()
    is InsulinDataDto           -> toDomain()
}

internal fun BaseData.toDto(): BaseDataDto = when (this) {
    is BasalAutomatedData    -> toDto()
    is BolusData             -> toDto()
    is ContinuousGlucoseData -> toDto()
    is DosingDecisionData    -> toDto()
    is FoodData              -> toDto()
    is InsulinData           -> toDto()
}

internal fun BaseData.DataType.toDto(): BaseDataDto.DataTypeDto = when (this) {
    BaseData.DataType.Alert              -> BaseDataDto.DataTypeDto.Alert
    BaseData.DataType.Basal              -> BaseDataDto.DataTypeDto.Basal
    BaseData.DataType.BloodKetone        -> BaseDataDto.DataTypeDto.BloodKetone
    BaseData.DataType.Bolus              -> BaseDataDto.DataTypeDto.Bolus
    BaseData.DataType.Calculator         -> BaseDataDto.DataTypeDto.Calculator
    BaseData.DataType.Cbg                -> BaseDataDto.DataTypeDto.Cbg
    BaseData.DataType.CgmSettings        -> BaseDataDto.DataTypeDto.CgmSettings
    BaseData.DataType.ControllerSettings -> BaseDataDto.DataTypeDto.ControllerSettings
    BaseData.DataType.ControllerStatus   -> BaseDataDto.DataTypeDto.ControllerStatus
    BaseData.DataType.DeviceEvent        -> BaseDataDto.DataTypeDto.DeviceEvent
    BaseData.DataType.DosingDecision     -> BaseDataDto.DataTypeDto.DosingDecision
    BaseData.DataType.Food               -> BaseDataDto.DataTypeDto.Food
    BaseData.DataType.Insulin            -> BaseDataDto.DataTypeDto.Insulin
    BaseData.DataType.PhysicalActivity   -> BaseDataDto.DataTypeDto.PhysicalActivity
    BaseData.DataType.PumpSettings       -> BaseDataDto.DataTypeDto.PumpSettings
    BaseData.DataType.PumpStatus         -> BaseDataDto.DataTypeDto.PumpStatus
    BaseData.DataType.ReportedState      -> BaseDataDto.DataTypeDto.ReportedState
    BaseData.DataType.Smbg               -> BaseDataDto.DataTypeDto.Smbg
}
