package org.tidepool.sdk

import kotlin.test.Test
import org.tidepool.sdk.requests.TokenRequest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import org.tidepool.sdk.model.auth.Realm
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import java.time.Instant

class TestClass {
	fun THE_test() = runBlocking {
		val helper = CommunicationHelper(Environments.Qa2)
		launch {
			val connectionResponse = helper.auth.obtainToken(Realm.qa2, TokenRequest.createWithPassword("client_id", "username", "password").build())
			val user = helper.users.getCurrentUserInfo(connectionResponse.access_token)
			val allData = helper.data.getDataForUser(connectionResponse.access_token, user.userid, types=arrayOf("cbg"))
			for (data in allData) {
				var glucoseData = data as? ContinuousGlucoseData
				if (glucoseData != null) {
					System.out.println("Glucose Data Found:")
					System.out.printf("Unit: %s%n", glucoseData.units)
					System.out.printf("Value: %.2f%n", glucoseData.value)
					System.out.printf("Trend: %s%n", glucoseData.trend)
					System.out.printf("TrendRate: %.2f%n", glucoseData.trendRate)
					System.out.printf("Time: %0\$tT:%<tL %0<td/%<tm/%<tY", glucoseData.time)
				} else {
					System.out.println("Data Found:")
					System.out.println(data)
				}
			}
		}
	}

	@Test
	fun dateTest() {
		val now = Instant.now()
		System.out.printf("%tc%n", now.toEpochMilli())
		System.out.printf("Time: %tT:%<tL %<td/%<tm/%<tY%n", now.toEpochMilli())
	}
}