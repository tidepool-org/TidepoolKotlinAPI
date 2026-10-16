package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Selects data to delete from a data set by origin, as iOS TidepoolService does. */
@Serializable
data class DataSelectorDto(
    @SerialName("origin")
    val origin: OriginDto,
) {
    @Serializable
    data class OriginDto(
        @SerialName("id")
        val id: String,
    )
}
