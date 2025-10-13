package org.tidepool.sdk.model.data

import io.mcarle.konvert.api.KonvertFrom
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
    
    @KonvertFrom(DosingDecisionDataDto::class, mapFunctionName = "fromDto")
    companion object {}
    
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
    ) {
        
        @KonvertFrom(DosingDecisionDataDto.CarbsOnBoardDto::class, mapFunctionName = "fromDto")
        companion object {}
    }
    
    data class InsulinOnBoard(
        val time: Instant? = null,
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionDataDto.InsulinOnBoardDto::class, mapFunctionName = "fromDto")
        companion object {}
    }
    
    data class RecommendedBasal(
        val rate: Double = -1.0,
        val duration: Double? = null,
    ) {
        
        @KonvertFrom(DosingDecisionDataDto.RecommendedBasalDto::class, mapFunctionName = "fromDto")
        companion object {}
    }
    
    data class RecommendedBolus(
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionDataDto.RecommendedBolusDto::class, mapFunctionName = "fromDto")
        companion object {}
    }
    
    data class RequestedBolus(
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionDataDto.RequestedBolusDto::class, mapFunctionName = "fromDto")
        companion object {}
    }
    
    data class Units(
        val bg: BloodGlucose.Units = BloodGlucose.Units.MilligramsPerDeciliter,
        val carb: Carb = Carb.Exchanges,
        val insulin: Insulin = Insulin.Units,
    ) {
        
        @KonvertFrom(DosingDecisionDataDto.UnitsDto::class, mapFunctionName = "fromDto")
        companion object {}
        
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