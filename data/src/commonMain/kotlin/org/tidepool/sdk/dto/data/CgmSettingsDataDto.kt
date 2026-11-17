package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.CgmSettingsData
import kotlin.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

/** Mirrors TidepoolKit's TCGMSettingsDatum. */
@Serializable
data class CgmSettingsDataDto(
    override val id: String = "",
    override val type: BaseDataDto.DataTypeDto = BaseDataDto.DataTypeDto.CgmSettings,
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

    @SerialName("firmwareVersion")
    val firmwareVersion: String? = null,
    @SerialName("hardwareVersion")
    val hardwareVersion: String? = null,
    @SerialName("manufacturers")
    val manufacturers: List<String>? = null,
    @SerialName("model")
    val model: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("softwareVersion")
    val softwareVersion: String? = null,
    @SerialName("transmitterId")
    val transmitterId: String? = null,
    @SerialName("units")
    val units: BloodGlucoseDto.UnitsDto? = null,
    @SerialName("origin")
    val origin: OriginDto? = null,
    @SerialName("payload")
    val payload: PayloadDto? = null,
) : BaseDataDto() {

    @Serializable
    data class PayloadDto(
        @SerialName("syncIdentifier")
        val syncIdentifier: String,
    )
}

fun CgmSettingsDataDto.toDomain(): CgmSettingsData = CgmSettingsData(
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
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    manufacturers = manufacturers,
    model = model,
    name = name,
    softwareVersion = softwareVersion,
    transmitterId = transmitterId,
    units = units?.toDomain(),
    origin = origin?.toDomain(),
    payload = payload?.toDomain(),
)

fun CgmSettingsData.toDto(): CgmSettingsDataDto = CgmSettingsDataDto(
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
    firmwareVersion = firmwareVersion,
    hardwareVersion = hardwareVersion,
    manufacturers = manufacturers,
    model = model,
    name = name,
    softwareVersion = softwareVersion,
    transmitterId = transmitterId,
    units = units?.toDto(),
    origin = origin?.toDto(),
    payload = payload?.toDto(),
)

private fun CgmSettingsDataDto.PayloadDto.toDomain() = CgmSettingsData.Payload(syncIdentifier = syncIdentifier)

private fun CgmSettingsData.Payload.toDto() = CgmSettingsDataDto.PayloadDto(syncIdentifier = syncIdentifier)
