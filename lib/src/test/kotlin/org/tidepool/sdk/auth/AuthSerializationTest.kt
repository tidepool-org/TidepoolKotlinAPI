package org.tidepool.sdk.auth

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthSerializationTest {
    
    private val json: Json by CommunicationHelper.Companion::jsonConfig
    
    @Test
    fun serializationTest() {
        val req = TokenRequest(
            GrantType.Password,
            "cgm-monitor",
            client_secret = "c50e6502-131c-47f0-b439-a43acb3b83d0",
            password = "qwertyuiop1234",
            username = "user@example.com"
        )
        val serialized = json.encodeToString(req)
        val expectedJson =
            "{\"grant_type\":\"password\",\"client_id\":\"cgm-monitor\",\"client_secret\":\"c50e6502-131c-47f0-b439-a43acb3b83d0\",\"subject_token\":null,\"subject_token_type\":null,\"requested_token_type\":null,\"subject_issuer\":null,\"username\":\"user@example.com\",\"password\":\"qwertyuiop1234\",\"code\":null,\"code_verifier\":null}"
        assertEquals(expectedJson, serialized)
        assertEquals(req, json.decodeFromString<TokenRequest>(serialized))
    }
    
    
    @Test
    fun serializationAnnotationTest() {
        val req = TokenRequest(GrantType.TokenExchange, "cgm-monitor")
        val serialized = json.encodeToString(req)
        val expectedJson =
            "{\"grant_type\":\"urn:ietf:params:oauth:grant-type:token-exchange\",\"client_id\":\"cgm-monitor\",\"client_secret\":null,\"subject_token\":null,\"subject_token_type\":null,\"requested_token_type\":null,\"subject_issuer\":null,\"username\":null,\"password\":null,\"code\":null,\"code_verifier\":null}"
        assertEquals(expectedJson, serialized)
        assertEquals(req, json.decodeFromString<TokenRequest>(serialized))
    }
}