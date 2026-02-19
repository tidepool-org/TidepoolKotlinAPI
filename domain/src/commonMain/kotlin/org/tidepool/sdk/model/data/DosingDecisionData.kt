package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import org.tidepool.sdk.model.BloodGlucose
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: finish implementing dosingdecision.v1
data class DosingDecisionData(
    override val id: String,
    override val type: DataType = DataType.DosingDecision,
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<Association> = emptyList(),
    override val clockDriftOffset: Duration? = null,
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    override val timeZone: TimeZone? = null,
    override val timeZoneOffset: Int? = null,
    val reason: String,
    val carbsOnBoard: CarbsOnBoard? = null,
    val insulinOnBoard: InsulinOnBoard? = null,
    val recommendedBasal: RecommendedBasal? = null,
    val recommendedBolus: RecommendedBolus? = null,
    val requestedBolus: RequestedBolus? = null,
    val scheduleTimeZoneOffset: Int? = null,
    val units: Units = Units(),
    val smbg: BloodGlucose.GlucoseReading? = null,
    val bgHistorical: List<BloodGlucose.GlucoseReading>? = null,
    val bgForecast: List<BloodGlucose.GlucoseReading>? = null,
) : BaseData(
    id = id,
    type = type,
    time = time,
    annotations = annotations,
    associations = associations,
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
) {
    
    val originalFood: Nothing
        get() = TODO("backing object not implemented")
    val food: Nothing
        get() = TODO("backing object not implemented")
    val bgTargetSchedule: Nothing
        get() = TODO("schema \"targetstart.v1\" not implemented")
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