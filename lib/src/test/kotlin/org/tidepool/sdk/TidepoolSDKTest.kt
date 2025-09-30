package org.tidepool.sdk

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest

class TidepoolSDKTest : KoinTest {
    
    @AfterEach
    fun cleanup() {
        TidepoolSDK.shutdown()
    }
    
    @Test
    fun `should initialize SDK successfully`() {
        // Given
        val environment = Environments.Dev1
        
        // When
        val sdk = TidepoolSDK.initialize(environment)
        
        // Then
        assertNotNull(sdk)
        assertEquals(sdk, TidepoolSDK.getInstance())
    }
    
    @Test
    fun `should throw exception when accessing uninitialized SDK`() {
        // Given - no initialization
        
        // When & Then
        assertThrows(IllegalStateException::class.java) {
            TidepoolSDK.getInstance()
        }
    }
    
    @Test
    fun `should allow reinitialization after shutdown`() {
        // Given
        val sdk1 = TidepoolSDK.initialize(Environments.Dev1)
        
        // When
        TidepoolSDK.shutdown()
        val sdk2 = TidepoolSDK.initialize(Environments.Production)
        
        // Then
        assertNotNull(sdk2)
        assertEquals(sdk2, TidepoolSDK.getInstance())
    }
}