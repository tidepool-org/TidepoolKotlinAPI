package org.tidepool.sdk.service

import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.toDto
import org.tidepool.sdk.repository.DataRepository
import java.time.Instant

class DataService internal constructor(
    private val dataRepository: DataRepository,
    private val tokenProvider: TokenProvider,
) {
    
    suspend fun getDataForUser(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<DataType> = emptyList(),
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        dexcom: Boolean? = null,
        carelink: Boolean? = null,
        medtronic: Boolean? = null,
    ): Result<List<BaseData>> = dataRepository.getDataForUser(
        userId = userId,
        uploadId = uploadId,
        deviceId = deviceId,
        types = types.map { it.toDto() },
        startDate = startDate,
        endDate = endDate,
        latest = latest,
        dexcom = dexcom,
        carelink = carelink,
        medtronic = medtronic,
        sessionToken = tokenProvider.getToken()
    ).mapList { BaseData.fromDto(it) }
}