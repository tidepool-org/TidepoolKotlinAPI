package org.tidepool.sdk.model.data

import com.google.gson.annotations.SerializedName
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
        val bg: BloodGlucose.Units = BloodGlucose.Units.milligramsPerDeciliter,
        val carb: Carb = Carb.Exchanges,
        val insulin: Insulin = Insulin.Units,
    ) {
        
        enum class Carb {
            @SerializedName("exchanges")
            Exchanges,
            
            @SerializedName("grams")
            Grams
        }
        
        enum class Insulin {
            Units,
        }
    }
}