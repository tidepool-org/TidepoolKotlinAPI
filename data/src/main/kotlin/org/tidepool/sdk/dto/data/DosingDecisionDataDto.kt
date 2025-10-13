package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.dto.BloodGlucoseDto
import java.time.Instant

// TODO: finish implementing dosingdecision.v1
@Serializable
data class DosingDecisionDataDto(
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