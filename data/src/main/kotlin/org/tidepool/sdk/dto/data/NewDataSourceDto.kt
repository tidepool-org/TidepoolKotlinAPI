package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.NewDataSource

@Serializable
@KonvertTo(NewDataSource::class, mapFunctionName = "toDomain")
data class NewDataSourceDto(
    @SerialName("providerName")
    val providerName: String? = null,
    @SerialName("providerSessionId")
    val providerSessionId: String? = null,
    @SerialName("providerType")
    val providerType: String? = null
) {
    @KonvertFrom(NewDataSource::class, mapFunctionName = "fromDomain")
    companion object {}
}