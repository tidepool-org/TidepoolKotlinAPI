package org.tidepool.sdk.deserialization

import kotlin.reflect.KClass

interface ResultType<T : Any> {
    
    val subclassType: KClass<out T>
}