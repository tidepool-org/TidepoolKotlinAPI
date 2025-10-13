package org.tidepool.sdk.repository

import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataType
import java.time.Instant

interface DataRepository {

    suspend fun getDataForUser(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<DataType>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        dexcom: Boolean? = null,
        carelink: Boolean? = null,
        medtronic: Boolean? = null,
        sessionToken: String,
    ): Result<List<BaseData>>
}