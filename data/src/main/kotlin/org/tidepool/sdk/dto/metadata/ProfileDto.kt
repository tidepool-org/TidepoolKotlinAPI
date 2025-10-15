package org.tidepool.sdk.dto.metadata

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.metadata.Profile

@Serializable
@KonvertTo(value = Profile::class, mapFunctionName = "toDomain")
open class ProfileDto(
    @SerialName("fullName")
    val fullName: String? = null,
)