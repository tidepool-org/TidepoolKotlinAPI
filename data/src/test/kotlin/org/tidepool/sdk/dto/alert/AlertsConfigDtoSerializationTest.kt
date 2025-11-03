package org.tidepool.sdk.dto.alert

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

class AlertConfigDtoSerializationTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }
    
    @Test
    fun `should serialize complete AlertConfig with mixed glucose units`() {
        val alertConfig = AlertConfigDto(
            uploadId = "upload123",
            userId = "user456",
            followedUserId = "followed789",
            urgentLow = UrgentGlucoseAlertDto(
                enabled = true,
                threshold = GlucoseDto.MgDLGlucoseDto(value = 55)
            ),
            low = GlucoseAlertDto(
                enabled = true,
                threshold = GlucoseDto.MgDLGlucoseDto(value = 70),
                delayMinutes = 15,
                repeatMinutes = 30
            ),
            high = GlucoseAlertDto(
                enabled = true,
                threshold = GlucoseDto.MmolGlucoseDto(value = 10.0f),
                delayMinutes = 30,
                repeatMinutes = 60
            ),
            noCommunication = ConnectionAlertDto(
                enabled = true,
                delayMinutes = 20
            ),
            notLooping = ConnectionAlertDto(
                enabled = false,
                delayMinutes = 45
            )
        )
        
        val jsonString = json.encodeToString(AlertConfigDto.serializer(), alertConfig)
        val deserializedConfig = json.decodeFromString(AlertConfigDto.serializer(), jsonString)
        
        assertEquals(alertConfig, deserializedConfig)
        
        // Verify specific glucose thresholds are correctly serialized
        assertContains(jsonString, "\"value\": 55")
        assertContains(jsonString, "\"units\": \"mg/dL\"")
        assertContains(jsonString, "\"value\": 10.0")
        assertContains(jsonString, "\"units\": \"mmol/L\"")
    }
    
    @Test
    fun `should deserialize AlertConfig from JSON with mixed glucose units`() {
        val jsonString = """
        {
            "uploadId": "upload123",
            "userId": "user456",
            "followedUserId": "followed789",
            "urgentLow": {
                "enabled": true,
                "threshold": {
                    "units": "mg/dL",
                    "value": 55
                }
            },
            "low": {
                "enabled": true,
                "threshold": {
                    "units": "mg/dL",
                    "value": 70
                },
                "delay": 15,
                "repeat": 30
            },
            "high": {
                "enabled": true,
                "threshold": {
                    "units": "mmol/L",
                    "value": 10.0
                },
                "delay": 30,
                "repeat": 60
            },
            "noCommunication": {
                "enabled": true,
                "delay": 20
            },
            "notLooping": {
                "enabled": false,
                "delay": 45
            }
        }
        """.trimIndent()
        
        val alertConfig = json.decodeFromString(AlertConfigDto.serializer(), jsonString)
        
        assertEquals("upload123", alertConfig.uploadId)
        assertEquals("user456", alertConfig.userId)
        assertEquals("followed789", alertConfig.followedUserId)
        
        // Verify glucose thresholds are correctly deserialized
        val urgentLowThreshold = alertConfig.urgentLow?.threshold
        assert(urgentLowThreshold is GlucoseDto.MgDLGlucoseDto)
        assertEquals(55, (urgentLowThreshold as GlucoseDto.MgDLGlucoseDto).value)
        
        val highThreshold = alertConfig.high?.threshold
        assert(highThreshold is GlucoseDto.MmolGlucoseDto)
        assertEquals(10.0f, (highThreshold as GlucoseDto.MmolGlucoseDto).value)
    }
}