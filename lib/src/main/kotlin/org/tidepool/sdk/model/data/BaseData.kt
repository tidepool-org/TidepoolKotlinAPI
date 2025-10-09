package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.BasalAutomatedDataDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.BolusDataDto
import org.tidepool.sdk.dto.data.ContinuousGlucoseDataDto
import org.tidepool.sdk.dto.data.DosingDecisionDataDto
import org.tidepool.sdk.dto.data.FoodDataDto
import org.tidepool.sdk.dto.data.InsulinDataDto
import org.tidepool.sdk.model.Association
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// TODO: finish implementing base.v1
sealed class BaseData(
    val type: DataType = DataType.Alert,
    val time: Instant? = null,
    val annotations: Array<Map<String, String>>? = null,
    val associations: Array<Association>? = null,
    val clockDriftOffset: Duration? = null,
    val conversionOffset: Duration? = null,
    val dataSetId: String? = null,
    val deviceTime: String? = null,
    val id: String? = null,
    val notes: Array<String>? = null,
    val timeZone: TimeZone? = null,
    val timeZoneOffset: Duration? = null
) {
    
    val location: Nothing
        get() = TODO("schema \"\" not implemented")
}

internal fun BaseDataDto.toDomain() = when (this) {
    is BasalAutomatedDataDto    -> toDomain()
    is BolusDataDto             -> toDomain()
    is ContinuousGlucoseDataDto -> toDomain()
    is DosingDecisionDataDto    -> toDomain()
    is FoodDataDto              -> toDomain()
    is InsulinDataDto           -> toDomain()
}
