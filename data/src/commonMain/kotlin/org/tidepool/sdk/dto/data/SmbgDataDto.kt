package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.SmbgData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** Mirrors TidepoolKit's TSMBGDatum. */
@Serializable
data class SmbgDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Smbg,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    @SerialName("timezone")
    override val timeZone: TimeZone? = null,
    @Contextual
    @SerialName("timezoneOffset")
    override val timeZoneOffset: Int? = null,

    @SerialName("value")
    val value: Double? = null,
    @SerialName("units")
    val units: BloodGlucoseDto.UnitsDto? = null,
    @SerialName("subType")
    val subType: SubTypeDto? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
    @SerialName("deviceId")
    val deviceId: String? = null,
) : BaseDataDto() {

    @Serializable
    enum class SubTypeDto {
        @SerialName("manual")
        Manual,

        @SerialName("linked")
        Linked,
    }

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
        @SerialName("syncVersion")
        val syncVersion: Int? = null,
    )
}

fun SmbgDataDto.toDomain(): SmbgData = SmbgData(
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
    subType = subType?.toDomain(),
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
    deviceId = deviceId,
)

fun SmbgData.toDto(): SmbgDataDto = SmbgDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations?.takeUnless { it.isEmpty() },
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
    subType = subType?.toDto(),
    origin = origin?.toDto(),
    payload = payload?.toDto(),
    deviceId = deviceId,
)

fun SmbgDataDto.SubTypeDto.toDomain(): SmbgData.SubType = when (this) {
    SmbgDataDto.SubTypeDto.Manual -> SmbgData.SubType.Manual
    SmbgDataDto.SubTypeDto.Linked -> SmbgData.SubType.Linked
}

fun SmbgData.SubType.toDto(): SmbgDataDto.SubTypeDto = when (this) {
    SmbgData.SubType.Manual -> SmbgDataDto.SubTypeDto.Manual
    SmbgData.SubType.Linked -> SmbgDataDto.SubTypeDto.Linked
}

private fun SmbgDataDto.PayloadDto.toDomain() = SmbgData.Payload(
    syncIdentifier = syncIdentifier,
    syncVersion = syncVersion,
)

private fun SmbgData.Payload.toDto() = SmbgDataDto.PayloadDto(
    syncIdentifier = syncIdentifier,
    syncVersion = syncVersion,
)
