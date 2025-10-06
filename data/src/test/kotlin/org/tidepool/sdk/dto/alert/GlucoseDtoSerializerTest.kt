package org.tidepool.sdk.dto.alert

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GlucoseDtoSerializerTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    @Test
    fun `should serialize and deserialize MgDL glucose`() {
        val glucose = GlucoseDto.MgDLGlucoseDto(value = 120)
        
        val jsonString = json.encodeToString(glucose)
        val deserializedGlucose = json.decodeFromString<GlucoseDto>(jsonString)
        
        assertEquals(glucose, deserializedGlucose)
        assert(jsonString.contains("\"units\":\"mg/dL\""))
        assert(jsonString.contains("\"value\":120"))
    }
    
    @Test
    fun `should serialize and deserialize Mmol glucose`() {
        val glucose = GlucoseDto.MmolGlucoseDto(value = 6.7f)
        
        val jsonString = json.encodeToString(glucose)
        val deserializedGlucose = json.decodeFromString<GlucoseDto>(jsonString)
        
        assertEquals(glucose, deserializedGlucose)
        assert(jsonString.contains("\"units\":\"mmol/L\""))
        assert(jsonString.contains("\"value\":6.7"))
    }
    
    @Test
    fun `should deserialize from JSON with mgdL units`() {
        val jsonString = """{"units":"mg/dL","value":85}"""
        
        val glucose = json.decodeFromString<GlucoseDto>(jsonString)
        
        assertEquals(GlucoseDto.MgDLGlucoseDto(value = 85), glucose)
    }
    
    @Test
    fun `should deserialize from JSON with mmolL units`() {
        val jsonString = """{"units":"mmol/L","value":4.7}"""
        
        val glucose = json.decodeFromString<GlucoseDto>(jsonString)
        
        assertEquals(GlucoseDto.MmolGlucoseDto(value = 4.7f), glucose)
    }
    
    @Test
    fun `should handle case insensitive units`() {
        val mgdlJson = """{"units":"MG/DL","value":100}"""
        val mmolJson = """{"units":"MMOL/L","value":5.5}"""
        
        val mgdlGlucose = json.decodeFromString<GlucoseDto>(mgdlJson)
        val mmolGlucose = json.decodeFromString<GlucoseDto>(mmolJson)
        
        assert(mgdlGlucose is GlucoseDto.MgDLGlucoseDto)
        assert(mmolGlucose is GlucoseDto.MmolGlucoseDto)
    }
}