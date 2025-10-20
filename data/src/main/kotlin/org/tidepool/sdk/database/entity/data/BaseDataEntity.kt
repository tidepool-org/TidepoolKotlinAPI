package org.tidepool.sdk.database.entity.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.FoodDataDto
import org.tidepool.sdk.dto.data.InsulinDataDto
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import java.time.Instant

@Entity
sealed class BaseDataEntity(
    @PrimaryKey
    open var id: String,
    open var type: String,
    open var time: Instant?,
    open var annotations: String?, // JSON string
    open var associations: String?, // JSON string
    open var clockDriftOffset: Long?, // Duration in milliseconds
    open var conversionOffset: Long?, // Duration in milliseconds
    open var dataSetId: String?,
    open var deviceTime: String?,
    open var notes: String?, // JSON string
    open var timeZone: String?, // TimeZone ID
    open var timeZoneOffset: Long?, // Duration in milliseconds
)

internal fun BaseDataEntity.toDomain(): BaseData = when (this) {
    is BasalAutomatedDataEntity    -> toDomain()
    is BolusDataEntity             -> toDomain()
    is ContinuousGlucoseDataEntity -> toDomain()
    is DosingDecisionDataEntity    -> toDomain()
    is FoodDataEntity              -> toDomain()
    is InsulinDataEntity           -> toDomain()
}

internal fun DataType.toEntity(): DataTypeEntity = when (this) {
    DataType.Alert              -> DataTypeEntity.Alert
    DataType.Basal              -> DataTypeEntity.Basal
    DataType.BloodKetone        -> DataTypeEntity.BloodKetone
    DataType.Bolus              -> DataTypeEntity.Bolus
    DataType.Calculator         -> DataTypeEntity.Calculator
    DataType.Cbg                -> DataTypeEntity.Cbg
    DataType.CgmSettings        -> DataTypeEntity.CgmSettings
    DataType.ControllerSettings -> DataTypeEntity.ControllerSettings
    DataType.ControllerStatus   -> DataTypeEntity.ControllerStatus
    DataType.DeviceEvent        -> DataTypeEntity.DeviceEvent
    DataType.DosingDecision     -> DataTypeEntity.DosingDecision
    DataType.Food               -> DataTypeEntity.Food
    DataType.Insulin            -> DataTypeEntity.Insulin
    DataType.PhysicalActivity   -> DataTypeEntity.PhysicalActivity
    DataType.PumpSettings       -> DataTypeEntity.PumpSettings
    DataType.PumpStatus         -> DataTypeEntity.PumpStatus
    DataType.ReportedState      -> DataTypeEntity.ReportedState
    DataType.Smbg               -> DataTypeEntity.Smbg
}

fun BaseDataDto.toEntity(): BaseDataEntity = when (this) {
    is BasalAutomatedDataDto    -> toEntity()
    is BolusDataDto             -> toEntity()
    is ContinuousGlucoseDataDto -> toEntity()
    is DosingDecisionDataDto    -> toEntity()
    is FoodDataDto              -> toEntity()
    is InsulinDataDto           -> toEntity()
    else -> throw IllegalArgumentException("Unknown BaseDataDto subtype: ${this::class}")
}