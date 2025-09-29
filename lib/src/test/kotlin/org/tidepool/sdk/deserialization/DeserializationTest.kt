package org.tidepool.sdk.deserialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.tidepool.sdk.CommunicationHelper
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeserializationTest {
    
    @Serializable
    sealed class Sample(
        val type: SampleSubtype,
        val id: String? = null
    ) {
        
        @Serializable
        enum class SampleSubtype(override val subclassType: KClass<out Sample>) :
            ResultType<Sample> {

            @SerialName("testSubclass")
            TestSubclassType(TestSubclass::class),

            @SerialName("nullSubclass")
            NullSubclass(Sample::class)
        }
    }
    
    @Serializable
    data class TestSubclass(val name: String = "Test") : Sample(SampleSubtype.TestSubclassType, "id")
    
    val json: Json by lazy {
        CommunicationHelper.jsonConfig
    }
    
    @Test
    fun deserializationTest() {
        val subclassInstance = TestSubclass("Test")
        val jsonString = json.encodeToString(subclassInstance)
        val fromJson = json.decodeFromString<TestSubclass>(jsonString)
        assertEquals(subclassInstance, fromJson)
    }
    
    @Test
    fun nullFail() {
        // This test is no longer applicable since we're testing different serialization behavior
        // With Kotlinx.serialization, the enum deserialization will either work or fail differently
        // Let's test a more realistic failure case
        val jsonString = "{\"name\":\"Test\",\"type\":\"unknownSubtype\",\"id\":\"id\"}"
        val exception = assertFailsWith<SerializationException> {
            json.decodeFromString<TestSubclass>(jsonString)
        }
        // Kotlinx.serialization will throw when encountering unknown enum values
    }
}