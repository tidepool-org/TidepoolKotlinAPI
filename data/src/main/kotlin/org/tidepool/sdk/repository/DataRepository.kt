package org.tidepool.sdk.repository

import org.tidepool.sdk.dto.data.BaseDataDto
import java.time.Instant

interface DataRepository {

    suspend fun getDataForUser(
        userId: String,
        uploadId: String? = null,
        deviceId: String? = null,
        types: List<BaseDataDto.DataTypeDto>? = null,
        startDate: Instant? = null,
        endDate: Instant? = null,
        latest: Boolean? = null,
        dexcom: Boolean? = null,
        carelink: Boolean? = null,
        medtronic: Boolean? = null,
        sessionToken: String,
    ): Result<List<BaseDataDto>>
}