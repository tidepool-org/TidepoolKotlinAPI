package org.tidepool.sdk.repository.impl

import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant

class DataRepositoryImpl(
    private val dataApi: DataApi,
) : DataRepository {
    
    override suspend fun getDataForUser(
        userId: String,
        uploadId: String?,
        deviceId: String?,
        types: List<BaseDataDto.DataTypeDto>?,
        startDate: Instant?,
        endDate: Instant?,
        latest: Boolean?,
        dexcom: Boolean?,
        carelink: Boolean?,
        medtronic: Boolean?,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        val typesParam = types
            ?.toTypedArray()
            ?.let { DataApi.CommaSeparatedArray(*it) }
        
        dataApi.getDataForUser(
            sessionToken = sessionToken,
            userId = userId,
            uploadId = uploadId,
            deviceId = deviceId,
            types = typesParam,
            startDate = startDate,
            endDate = endDate,
            latest = latest,
            dexcom = dexcom,
            carelink = carelink,
            medtronic = medtronic
        )
    }
}