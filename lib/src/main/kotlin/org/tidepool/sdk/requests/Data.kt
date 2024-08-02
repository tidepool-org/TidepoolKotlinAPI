package org.tidepool.sdk.requests

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Header
import java.time.Instant
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.BaseData.DataType

interface Data {
	@GET("/data/{userId}")
	suspend fun getDataForUser(
		@Header("X-Tidepool-Session-Token") sessionToken: String,
		@Path("userId") userId: String,
		@Query("uploadId") uploadId: String? = null,
		@Query("deviceId") deviceId: String? = null,
		@Query("type", encoded = true) types: CommaSeparatedArray<DataType>? = null,
		//TODO: Add subtype
		@Query("startDate") startDate: Instant? = null,
		@Query("endDate") endDate: Instant? = null,
		@Query("latest") latest: Boolean? = null,
		@Query("dexcom") dexcom: Boolean? = null,
		@Query("carelink") carelink: Boolean? = null,
		@Query("medtronic") medtronic: Boolean? = null
	) : Array<BaseData>
	
	class CommaSeparatedArray<T>(private vararg val types: T) {
		
		override fun toString(): String {
			var result = ""
			for (type in types) {
				result += "$type,"
			}
			return result.substring(0, result.lastIndex)
		}
	}

}