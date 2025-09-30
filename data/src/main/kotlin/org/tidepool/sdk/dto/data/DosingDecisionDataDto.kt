package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.dto.BloodGlucoseDto
import java.time.Instant

// TODO: finish implementing dosingdecision.v1
@Serializable
data class DosingDecisionDataDto(
    val reason: String,
    val carbsOnBoard: CarbsOnBoardDto? = null,
    val insulinOnBoard: InsulinOnBoardDto? = null,
    val recommendedBasal: RecommendedBasalDto? = null,
    val recommendedBolus: RecommendedBolusDto? = null,
    val requestedBolus: RequestedBolusDto? = null,
    val scheduleTimeZoneOffset: Int? = null,
    val units: UnitsDto = UnitsDto(),
) : BaseDataDto(DataTypeDto.DosingDecision) {
    
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
        @Contextual val time: Instant? = null,
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class InsulinOnBoardDto(
        @Contextual val time: Instant? = null,
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class RecommendedBasalDto(
        val rate: Double = -1.0,
        val duration: Double? = null,
    )
    
    @Serializable
    data class RecommendedBolusDto(
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class RequestedBolusDto(
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class UnitsDto(
        val bg: BloodGlucoseDto.UnitsDto = BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter,
        val carb: CarbDto = CarbDto.Exchanges,
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
            
            Units,
        }
    }
}