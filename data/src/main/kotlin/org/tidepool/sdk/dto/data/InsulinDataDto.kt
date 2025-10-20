package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.InsulinData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema insulin.v1
@Serializable
@KonvertTo(InsulinData::class, mapFunctionName = "toDomain")
data class InsulinDataDto(
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
    
    @SerialName("dose")
    val dose: DoseDto,
    @SerialName("site")
    val site: String?
) : BaseDataDto() {
    
    @KonvertFrom(InsulinData::class, mapFunctionName = "fromDomain")
    companion object {}
    
    val formulation: Nothing
        get() = TODO("schema \"formulation.v1\" not implemented")
}