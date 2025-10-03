package org.tidepool.sdk.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.model.BloodGlucose
import java.time.Instant

// TODO: finish implementing dosingdecision.v1
@Serializable
data class DosingDecisionData(
    val reason: String,
    val carbsOnBoard: CarbsOnBoard? = null,
    val insulinOnBoard: InsulinOnBoard? = null,
    val recommendedBasal: RecommendedBasal? = null,
    val recommendedBolus: RecommendedBolus? = null,
    val requestedBolus: RequestedBolus? = null,
    val scheduleTimeZoneOffset: Int? = null,
    val units: Units = Units(),
) : BaseData(DataType.dosingDecision) {
    
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
    data class CarbsOnBoard(
        @Contextual val time: Instant? = null,
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class InsulinOnBoard(
        @Contextual val time: Instant? = null,
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class RecommendedBasal(
        val rate: Double = -1.0,
        val duration: Double? = null,
    )
    
    @Serializable
    data class RecommendedBolus(
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class RequestedBolus(
        val amount: Double = -1.0,
    )
    
    @Serializable
    data class Units(
        val bg: BloodGlucose.Units = BloodGlucose.Units.milligramsPerDeciliter,
        val carb: Carb = Carb.Exchanges,
        val insulin: Insulin = Insulin.Units,
    ) {
        
        @Serializable
        enum class Carb {
            
            @SerialName("exchanges")
            Exchanges,
            
            @SerialName("grams")
            Grams
        }
        
        @Serializable
        enum class Insulin {
            Units,
        }
    }
}