package org.tidepool.sdk.dto

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data class AssociationDto(
    val type: AssociationTypeDto?,
    val id: String?,
    val url: String?,
    val reason: String?
) {
    
    @Serializable
    enum class AssociationTypeDto(val subclassType: KClass<AssociationDto>) {
        
        blob(AssociationDto::class),
        datum(AssociationDto::class),
        image(AssociationDto::class),
        url(AssociationDto::class)
    }
}