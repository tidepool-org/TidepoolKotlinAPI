package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.NewDataSource

@Serializable
data class NewDataSourceDto(
    @SerialName("providerName")
    val providerName: String? = null,
    @SerialName("providerSessionId")
    val providerSessionId: String? = null,
    @SerialName("providerType")
    val providerType: String? = null
)

fun NewDataSourceDto.toDomain(): NewDataSource = NewDataSource(
    providerName = providerName,
    providerSessionId = providerSessionId,
    providerType = providerType,
)

fun NewDataSource.toDto(): NewDataSourceDto = NewDataSourceDto(
    providerName = providerName,
    providerSessionId = providerSessionId,
    providerType = providerType,
)