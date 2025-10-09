package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.model.BloodGlucose
import java.time.Instant

// TODO: finish implementing dosingdecision.v1
data class DosingDecisionData(
    val reason: String,
    val carbsOnBoard: CarbsOnBoard? = null,
    val insulinOnBoard: InsulinOnBoard? = null,
    val recommendedBasal: RecommendedBasal? = null,
    val recommendedBolus: RecommendedBolus? = null,
    val requestedBolus: RequestedBolus? = null,
    val scheduleTimeZoneOffset: Int? = null,
    val units: Units = Units(),
) : BaseData(DataType.DosingDecision) {
    
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
    
    data class CarbsOnBoard(
        val time: Instant? = null,
        val amount: Double = -1.0,
    )
    
    data class InsulinOnBoard(
        val time: Instant? = null,
        val amount: Double = -1.0,
    )
    
    data class RecommendedBasal(
        val rate: Double = -1.0,
        val duration: Double? = null,
    )
    
    data class RecommendedBolus(
        val amount: Double = -1.0,
    )
    
    data class RequestedBolus(
        val amount: Double = -1.0,
    )
    
    data class Units(
        val bg: BloodGlucose.Units = BloodGlucose.Units.MilligramsPerDeciliter,
        val carb: Carb = Carb.Exchanges,
        val insulin: Insulin = Insulin.Units,
    ) {
        
        enum class Carb {
            Exchanges,
            Grams,
            ;
        }
        
        enum class Insulin {
            Units,
        }
    }
}

internal fun DosingDecisionDataDto.toDomain() = DosingDecisionData(
    reason = reason,
    carbsOnBoard = carbsOnBoard?.toDomain(),
    insulinOnBoard = insulinOnBoard?.toDomain(),
    recommendedBasal = recommendedBasal?.toDomain(),
    recommendedBolus = recommendedBolus?.toDomain(),
    requestedBolus = requestedBolus?.toDomain(),
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    units = units.toDomain(),
)

internal fun DosingDecisionDataDto.CarbsOnBoardDto.toDomain() = DosingDecisionData.CarbsOnBoard(
    time = time,
    amount = amount,
)

internal fun DosingDecisionDataDto.InsulinOnBoardDto.toDomain() = DosingDecisionData.InsulinOnBoard(
    time = time,
    amount = amount,
)

internal fun DosingDecisionDataDto.RecommendedBasalDto.toDomain() =
    DosingDecisionData.RecommendedBasal(
    rate = rate,
    duration = duration,
)

internal fun DosingDecisionDataDto.RecommendedBolusDto.toDomain() =
    DosingDecisionData.RecommendedBolus(
    amount = amount,
)

internal fun DosingDecisionDataDto.RequestedBolusDto.toDomain() = DosingDecisionData.RequestedBolus(
    amount = amount,
)

internal fun DosingDecisionDataDto.UnitsDto.toDomain() = DosingDecisionData.Units(
    bg = bg.toDomain(),
    carb = carb.toDomain(),
    insulin = insulin.toDomain(),
)

internal fun DosingDecisionDataDto.UnitsDto.InsulinDto.toDomain() = when (this) {
    DosingDecisionDataDto.UnitsDto.InsulinDto.Units -> DosingDecisionData.Units.Insulin.Units
}

internal fun DosingDecisionDataDto.UnitsDto.CarbDto.toDomain() = when (this) {
    DosingDecisionDataDto.UnitsDto.CarbDto.Exchanges -> DosingDecisionData.Units.Carb.Exchanges
    DosingDecisionDataDto.UnitsDto.CarbDto.Grams     -> DosingDecisionData.Units.Carb.Grams
}

internal fun BloodGlucoseDto.UnitsDto.toDomain() = when (this) {
    BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter -> BloodGlucose.Units.MilligramsPerDeciliter
    BloodGlucoseDto.UnitsDto.MillimolesPerLiter     -> BloodGlucose.Units.MillimolesPerLiter
}

internal fun DosingDecisionData.toDto() = DosingDecisionDataDto(
    reason = reason,
    carbsOnBoard = carbsOnBoard?.toDto(),
    insulinOnBoard = insulinOnBoard?.toDto(),
    recommendedBasal = recommendedBasal?.toDto(),
    recommendedBolus = recommendedBolus?.toDto(),
    requestedBolus = requestedBolus?.toDto(),
    scheduleTimeZoneOffset = scheduleTimeZoneOffset,
    units = units.toDto(),
)

internal fun DosingDecisionData.CarbsOnBoard.toDto() = DosingDecisionDataDto.CarbsOnBoardDto(
    time = time,
    amount = amount,
)

internal fun DosingDecisionData.InsulinOnBoard.toDto() = DosingDecisionDataDto.InsulinOnBoardDto(
    time = time,
    amount = amount,
)

internal fun DosingDecisionData.RecommendedBasal.toDto() = DosingDecisionDataDto.RecommendedBasalDto(
    rate = rate,
    duration = duration,
)

internal fun DosingDecisionData.RecommendedBolus.toDto() = DosingDecisionDataDto.RecommendedBolusDto(
    amount = amount,
)

internal fun DosingDecisionData.RequestedBolus.toDto() = DosingDecisionDataDto.RequestedBolusDto(
    amount = amount,
)

internal fun DosingDecisionData.Units.toDto() = DosingDecisionDataDto.UnitsDto(
    bg = bg.toDto(),
    carb = carb.toDto(),
    insulin = insulin.toDto(),
)

internal fun DosingDecisionData.Units.Insulin.toDto() = when (this) {
    DosingDecisionData.Units.Insulin.Units -> DosingDecisionDataDto.UnitsDto.InsulinDto.Units
}

internal fun DosingDecisionData.Units.Carb.toDto() = when (this) {
    DosingDecisionData.Units.Carb.Exchanges -> DosingDecisionDataDto.UnitsDto.CarbDto.Exchanges
    DosingDecisionData.Units.Carb.Grams     -> DosingDecisionDataDto.UnitsDto.CarbDto.Grams
}

internal fun BloodGlucose.Units.toDto() = when (this) {
    BloodGlucose.Units.MilligramsPerDeciliter -> BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter
    BloodGlucose.Units.MillimolesPerLiter     -> BloodGlucoseDto.UnitsDto.MillimolesPerLiter
}