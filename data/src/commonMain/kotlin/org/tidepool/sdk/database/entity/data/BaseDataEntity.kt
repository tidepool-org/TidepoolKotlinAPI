package org.tidepool.sdk.database.entity.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.touchlab.kermit.Logger
import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.FoodDataDto
import org.tidepool.sdk.dto.data.InsulinDataDto
import org.tidepool.sdk.dto.data.DeviceEventDataDto
import org.tidepool.sdk.dto.data.CgmSettingsDataDto
import org.tidepool.sdk.dto.data.ControllerSettingsDataDto
import org.tidepool.sdk.dto.data.PumpSettingsDataDto
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import kotlinx.datetime.Instant

@Entity
sealed class BaseDataEntity(
    @PrimaryKey
    open var id: String,
    open var type: String,
    open var time: Long?,
    open var annotations: String?, // JSON string
    open var associations: String?, // JSON string
    open var clockDriftOffset: Long?, // Duration in milliseconds
    open var conversionOffset: Long?, // Duration in milliseconds
    open var dataSetId: String?,
    open var deviceTime: String?,
    open var notes: String?, // JSON string
    open var timeZone: String?, // TimeZone ID
    open var timeZoneOffset: Int?, // Duration in minutes
)

internal fun BaseDataEntity.toDomain(): BaseData = when (this) {
    is BasalAutomatedDataEntity     -> toDomain()
    is BolusDataEntity              -> toDomain()
    is ContinuousGlucoseDataEntity  -> toDomain()
    is DosingDecisionDataEntity     -> toDomain()
    is CgmSettingsDataEntity        -> toDomain()
    is ControllerSettingsDataEntity -> toDomain()
    is FoodDataEntity               -> toDomain()
    is InsulinDataEntity            -> toDomain()
    is DeviceEventDataEntity        -> toDomain()
    is PumpSettingsDataEntity       -> toDomain()
}

internal fun BaseDataEntity.toDto(): BaseDataDto {
    Logger.d("BaseDataEntity") { "toDto(): ${javaClass.simpleName}, ${annotations?.let { "\"$it\"" }}"}
    return when (this) {
        is BasalAutomatedDataEntity -> toDto()
        is BolusDataEntity -> toDto()
        is ContinuousGlucoseDataEntity -> toDto()
        is DosingDecisionDataEntity -> toDto()
        is CgmSettingsDataEntity -> toDto()
        is ControllerSettingsDataEntity -> toDto()
        is FoodDataEntity -> toDto()
        is InsulinDataEntity -> toDto()
        is DeviceEventDataEntity -> toDto()
        is PumpSettingsDataEntity -> toDto()
    }
}

fun BaseDataDto.toEntity(): BaseDataEntity = when (this) {
    is BasalAutomatedDataDto        -> toEntity()
    is BolusDataDto                 -> toEntity()
    is ContinuousGlucoseDataDto     -> toEntity()
    is DosingDecisionDataDto        -> toEntity()
    is CgmSettingsDataDto           -> toEntity()
    is ControllerSettingsDataDto    -> toEntity()
    is FoodDataDto                  -> toEntity()
    is InsulinDataDto               -> toEntity()
    is DeviceEventDataDto           -> toEntity()
    is PumpSettingsDataDto          -> toEntity()
    else                            -> throw IllegalArgumentException(
        "Unknown BaseDataDto subtype: ${this::class}"
    )
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