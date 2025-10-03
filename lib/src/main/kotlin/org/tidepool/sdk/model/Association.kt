package org.tidepool.sdk.model

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
public data class Association(
    val type: AssociationType?,
    val id: String?,
    val url: String?,
    val reason: String?
) {
    
    @Serializable
    enum class AssociationType(val subclassType: KClass<Association>) {
        blob(Association::class),
        datum(Association::class),
        image(Association::class),
        url(Association::class)
    }
}