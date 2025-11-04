package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.DosingDecisionData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: finish implementing dosingdecision.v1
@Serializable
@KonvertTo(DosingDecisionData::class, mapFunctionName = "toDomain")
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
    
    @KonvertFrom(DosingDecisionData::class, mapFunctionName = "fromDomain")
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
    
    @Serializable
    @KonvertTo(DosingDecisionData.CarbsOnBoard::class, mapFunctionName = "toDomain")
    data class CarbsOnBoardDto(
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("amount")
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionData.CarbsOnBoard::class, mapFunctionName = "fromDomain")
        companion object {}
    }
    
    @Serializable
    @KonvertTo(DosingDecisionData.InsulinOnBoard::class, mapFunctionName = "toDomain")
    data class InsulinOnBoardDto(
        @Contextual
        @SerialName("time")
        val time: Instant? = null,
        @SerialName("amount")
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionData.InsulinOnBoard::class, mapFunctionName = "fromDomain")
        companion object {}
    }
    
    @Serializable
    @KonvertTo(DosingDecisionData.RecommendedBasal::class, mapFunctionName = "toDomain")
    data class RecommendedBasalDto(
        @SerialName("rate")
        val rate: Double = -1.0,
        @SerialName("duration")
        val duration: Double? = null,
    ) {
        
        @KonvertFrom(DosingDecisionData.RecommendedBasal::class, mapFunctionName = "fromDomain")
        companion object {}
    }
    
    @Serializable
    @KonvertTo(DosingDecisionData.RecommendedBolus::class, mapFunctionName = "toDomain")
    data class RecommendedBolusDto(
        @SerialName("amount")
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionData.RecommendedBolus::class, mapFunctionName = "fromDomain")
        companion object {}
    }
    
    @Serializable
    @KonvertTo(DosingDecisionData.RequestedBolus::class, mapFunctionName = "toDomain")
    data class RequestedBolusDto(
        @SerialName("amount")
        val amount: Double = -1.0,
    ) {
        
        @KonvertFrom(DosingDecisionData.RequestedBolus::class, mapFunctionName = "fromDomain")
        companion object {}
    }
    
    @Serializable
    @KonvertTo(DosingDecisionData.Units::class, mapFunctionName = "toDomain")
    data class UnitsDto(
        @SerialName("bg")
        val bg: BloodGlucoseDto.UnitsDto = BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter,
        @SerialName("carb")
        val carb: CarbDto = CarbDto.Exchanges,
        @SerialName("insulin")
        val insulin: InsulinDto = InsulinDto.Units,
    ) {
        
        @KonvertFrom(DosingDecisionData.Units::class, mapFunctionName = "fromDomain")
        companion object {}
        
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