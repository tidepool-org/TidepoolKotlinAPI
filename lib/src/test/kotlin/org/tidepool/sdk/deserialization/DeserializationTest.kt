package org.tidepool.sdk.deserialization

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.tidepool.sdk.CommunicationHelper
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeserializationTest {
    sealed class Sample(
        val type: SampleSubtype,
        val id: String? = null
    ) {
        
        enum class SampleSubtype(override val subclassType: KClass<out Sample>) :
            ResultType<Sample> {
            
            testSubclass(TestSubclass::class),
            nullSubclass(Sample::class)
        }
    }
    
    data class TestSubclass(val name: String = "Test") : Sample(SampleSubtype.testSubclass, "id")
    
    val gson: Gson by lazy {
        CommunicationHelper.gsonBuilder.apply {
            registerNewDeserializer<Sample.SampleSubtype, Sample>()
        }.create()
    }
    
    @Test
    fun deserializationTest() {
        val subclassInstance = TestSubclass("Test")
        val json = gson.toJson(subclassInstance)
        val fromJson = gson.fromJson(json, Sample::class.java)
        assert(fromJson is TestSubclass)
        val deserialized = fromJson as TestSubclass
        assertEquals(subclassInstance, deserialized)
    }
    
    @Test
    fun nullFail() {
        val json = "{\"type\":\"nullSubclass\"}"
        val exception = assertFailsWith<JsonSyntaxException> {
            gson.fromJson(json, Sample::class.java)
        }
        // Gson wraps the exception
        assertEquals(exception.cause, defaultDeserializationException)
    }
}