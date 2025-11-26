package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.clinic.Site

@Serializable
data class SiteDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("address")
    val address: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("postalCode")
    val postalCode: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("country")
    val country: String? = null,
)

public fun SiteDto.toDomain(): Site = Site(
    id = id,
    name = name,
    address = address,
    city = city,
    postalCode = postalCode,
    state = state,
    country = country
)

public fun Site.toDto(): SiteDto = SiteDto(
    id = id,
    name = name,
    address = address,
    city = city,
    postalCode = postalCode,
    state = state,
    country = country
)