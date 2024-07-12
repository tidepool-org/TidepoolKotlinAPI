package org.tidepool.sdk.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.lazy
import com.google.gson.Gson
import org.tidepool.sdk.CommunicationHelper

class AuthSerializationTest {
	val gson: Gson by CommunicationHelper.Companion::gsonConfig

	@Test
	fun serializationTest() {
		val req = TokenRequest(GrantType.password, "cgm-monitor", client_secret="c50e6502-131c-47f0-b439-a43acb3b83d0", password="qwertyuiop1234", username="user@example.com")
		val serialized = gson.toJson(req)
		assertEquals("{\"grant_type\":\"password\",\"client_id\":\"cgm-monitor\",\"client_secret\":\"c50e6502-131c-47f0-b439-a43acb3b83d0\",\"username\":\"user@example.com\",\"password\":\"qwertyuiop1234\"}", serialized)
		assertEquals(req, gson.fromJson(serialized, TokenRequest::class.java))
	}


	@Test
	fun serializationAnnotationTest() {
		val req = TokenRequest(GrantType.tokenExchange, "cgm-monitor")
		val serialized = gson.toJson(req)
		assertEquals("{\"grant_type\":\"urn:ietf:params:oauth:grant-type:token-exchange\",\"client_id\":\"cgm-monitor\"}", serialized)
		assertEquals(req, gson.fromJson(serialized, TokenRequest::class.java))
	}
}