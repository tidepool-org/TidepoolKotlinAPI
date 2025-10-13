package org.tidepool.sdk.repository

import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.toDomain
import org.tidepool.sdk.dto.data.toDto
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.runCatchingNetworkExceptions
import java.time.Instant

class DataRepositoryImpl(
    private val dataApi: DataApi,
) : DataRepository {
    
    override suspend fun getDataForUser(
        userId: String,
        uploadId: String?,
        deviceId: String?,
        types: List<DataType>?,
        startDate: Instant?,
        endDate: Instant?,
        latest: Boolean?,
        dexcom: Boolean?,
        carelink: Boolean?,
        medtronic: Boolean?,
        sessionToken: String
    ): Result<List<BaseData>> = runCatchingNetworkExceptions {
        val typesParam = types
            ?.map { it.toDto() }
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
    }.mapList { it.toDomain() }
}