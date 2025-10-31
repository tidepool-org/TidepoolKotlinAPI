package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto.*
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.data.DosingDecisionData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: finish implementing dosingdecision.v1
@Serializable
data class DosingDecisionDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>> = emptyList(),
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    override val timeZone: TimeZone? = null,
    @Contextual
    override val timeZoneOffset: Duration? = null,
    
    @SerialName("reason")
    val reason: String,
    @SerialName("carbsOnBoard")
    val carbsOnBoard: CarbsOnBoardDto? = null,
    @SerialName("insulinOnBoard")
    val insulinOnBoard: InsulinOnBoardDto? = null,
    @SerialName("recommendedBasal")
    val recommendedBasal: RecommendedBasalDto? = null,
    @SerialName("recommendedBolus")
    val recommendedBolus: RecommendedBolusDto? = null,
    @SerialName("requestedBolus")
    val requestedBolus: RequestedBolusDto? = null,
    @SerialName("scheduleTimeZoneOffset")
    val scheduleTimeZoneOffset: Int? = null,
    @SerialName("units")
    val units: UnitsDto = UnitsDto(),
) : BaseDataDto() {
    
    val originalFood: Nothing
        get() = TODO("backing object not implemented")
    val food: Nothing
        get() = TODO("backing object not implemented")
    val smbg: Nothing
        get() = TODO("schema \"bloodglucose.v1\" not implemented")
    val bgTargetSchedule: Nothing
        get() = TODO("schema \"targetstart.v1\" not implemented")
    val bgHistorical: Nothing
        get() = TODO("schema \"bloodglucose.v1\" not implemented")
    val bgForecast: Nothing
        get() = TODO("schema \"bloodglucose.v1\" not implemented")
    val warnings: Nothing
        get() = TODO("schema \"issue.v1\" not implemented")
    val errors: Nothing
        get() = TODO("schema \"issue.v1\" not implemented")
    
    @Serializable
    data class CarbsOnBoardDto(
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("amount")
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class InsulinOnBoardDto(
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("amount")
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class RecommendedBasalDto(
        @SerialName("rate")
        val rate: Double = -1.0,
        @SerialName("duration")
        val duration: Double? = null,
    )
    
    @Serializable
    data class RecommendedBolusDto(
        @SerialName("amount")
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class RequestedBolusDto(
        @SerialName("amount")
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class UnitsDto(
        @SerialName("bg")
        val bg: BloodGlucoseDto.UnitsDto = BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter,
        @SerialName("carb")
        val carb: CarbDto = CarbDto.Exchanges,
        @SerialName("insulin")
        val insulin: InsulinDto = InsulinDto.Units,
    ) {
        @Serializable
        enum class CarbDto {
            
            @SerialName("exchanges")
            Exchanges,
            
            @SerialName("grams")
            Grams
        }
        
        @Serializable
        enum class InsulinDto {
            
            @SerialName("units")
            Units,
        }
    }
}

internal fun UnitsDto.InsulinDto.toDomain() = when (this) {
    UnitsDto.InsulinDto.Units -> DosingDecisionData.Units.Insulin.Units
}

internal fun UnitsDto.CarbDto.toDomain() = when (this) {
    UnitsDto.CarbDto.Exchanges -> DosingDecisionData.Units.Carb.Exchanges
    UnitsDto.CarbDto.Grams     -> DosingDecisionData.Units.Carb.Grams
}

internal fun BloodGlucoseDto.UnitsDto.toDomain() = when (this) {
    BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter -> BloodGlucose.Units.MilligramsPerDeciliter
    BloodGlucoseDto.UnitsDto.MillimolesPerLiter     -> BloodGlucose.Units.MillimolesPerLiter
}

internal fun DosingDecisionData.Units.Insulin.toDto() = when (this) {
    DosingDecisionData.Units.Insulin.Units -> UnitsDto.InsulinDto.Units
}

internal fun DosingDecisionData.Units.Carb.toDto() = when (this) {
    DosingDecisionData.Units.Carb.Exchanges -> UnitsDto.CarbDto.Exchanges
    DosingDecisionData.Units.Carb.Grams     -> UnitsDto.CarbDto.Grams
}

internal fun BloodGlucose.Units.toDto() = when (this) {
    BloodGlucose.Units.MilligramsPerDeciliter -> BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter
    BloodGlucose.Units.MillimolesPerLiter     -> BloodGlucoseDto.UnitsDto.MillimolesPerLiter
}

fun CarbsOnBoardDto.toDomain() = DosingDecisionData.CarbsOnBoard(
    time = time,
    amount = amount,
)

fun DosingDecisionData.CarbsOnBoard.toDto() = CarbsOnBoardDto(
    time = time,
    amount = amount,
)

fun DosingDecisionDataDto.toDomain(): DosingDecisionData = DosingDecisionData(
    id = id,
    type = type.toDomain(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDomain() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    reason = reason,
    carbsOnBoard = carbsOnBoard?.toDomain(),
    insulinOnBoard = insulinOnBoard?.toDomain(),
    recommendedBasal = recommendedBasal?.toDomain(),
    recommendedBolus = recommendedBolus?.toDomain(),
    requestedBolus = requestedBolus?.toDomain(),
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    units = units.toDomain(),
)

fun DosingDecisionData.toDto(): DosingDecisionDataDto = DosingDecisionDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    reason = reason,
    carbsOnBoard = carbsOnBoard?.toDto(),
    insulinOnBoard = insulinOnBoard?.toDto(),
    recommendedBasal = recommendedBasal?.toDto(),
    recommendedBolus = recommendedBolus?.toDto(),
    requestedBolus = requestedBolus?.toDto(),
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    units = units.toDto(),
)

fun InsulinOnBoardDto.toDomain() = DosingDecisionData.InsulinOnBoard(
    time = time,
    amount = amount,
)

fun DosingDecisionData.InsulinOnBoard.toDto() = InsulinOnBoardDto(
    time = time,
    amount = amount,
)

fun RecommendedBasalDto.toDomain() = DosingDecisionData.RecommendedBasal(
    rate = rate,
    duration = duration,
)

fun DosingDecisionData.RecommendedBasal.toDto() = RecommendedBasalDto(
    rate = rate,
    duration = duration,
)

fun RecommendedBolusDto.toDomain() = DosingDecisionData.RecommendedBolus(
    amount = amount,
)

fun DosingDecisionData.RecommendedBolus.toDto() = RecommendedBolusDto(
    amount = amount,
)

fun RequestedBolusDto.toDomain() = DosingDecisionData.RequestedBolus(
    amount = amount
)

fun DosingDecisionData.RequestedBolus.toDto() = RequestedBolusDto(
    amount = amount
)