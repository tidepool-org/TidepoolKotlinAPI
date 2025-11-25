package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.BloodGlucoseDto.GlucoseReadingDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

@Serializable
data class ContinuousGlucoseDataDto(
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
    override val timeZoneOffset: Int? = null,

    @SerialName("value")
    val value: Double? = null,
    @SerialName("units")
    val units: BloodGlucoseDto.UnitsDto? = null,
    @SerialName("trend")
    val trend: BloodGlucoseDto.TrendDto? = null,
    @SerialName("trendRate")
    val trendRate: Double? = null,
) : BaseDataDto() {

    val reading: GlucoseReadingDto? by lazy {
        value?.let { value ->
            units?.let { units ->
                GlucoseReadingDto(value, units, time)
            }
        }
    }
}

fun ContinuousGlucoseDataDto.toDomain(): ContinuousGlucoseData = ContinuousGlucoseData(
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
    value = value,
    units = units?.toDomain(),
    trend = trend?.toDomain(),
    trendRate = trendRate,
)

fun ContinuousGlucoseData.toDto() = ContinuousGlucoseDataDto(
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
    value = value,
    units = units?.toDto(),
    trend = trend?.toDto(),
    trendRate = trendRate,
)