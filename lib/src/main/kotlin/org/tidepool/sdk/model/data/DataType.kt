package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.BaseDataDto.DataTypeDto

enum class DataType {
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
    Smbg,
    ;
}

internal fun DataType.toDto(): DataTypeDto = when (this) {
    DataType.Alert              -> DataTypeDto.Alert
    DataType.Basal              -> DataTypeDto.Basal
    DataType.BloodKetone        -> DataTypeDto.BloodKetone
    DataType.Bolus              -> DataTypeDto.Bolus
    DataType.Calculator         -> DataTypeDto.Calculator
    DataType.Cbg                -> DataTypeDto.Cbg
    DataType.CgmSettings        -> DataTypeDto.CgmSettings
    DataType.ControllerSettings -> DataTypeDto.ControllerSettings
    DataType.ControllerStatus   -> DataTypeDto.ControllerStatus
    DataType.DeviceEvent        -> DataTypeDto.DeviceEvent
    DataType.DosingDecision     -> DataTypeDto.DosingDecision
    DataType.Food               -> DataTypeDto.Food
    DataType.Insulin            -> DataTypeDto.Insulin
    DataType.PhysicalActivity   -> DataTypeDto.PhysicalActivity
    DataType.PumpSettings       -> DataTypeDto.PumpSettings
    DataType.PumpStatus         -> DataTypeDto.PumpStatus
    DataType.ReportedState      -> DataTypeDto.ReportedState
    DataType.Smbg               -> DataTypeDto.Smbg
}