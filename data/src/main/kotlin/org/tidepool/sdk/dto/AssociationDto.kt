package org.tidepool.sdk.dto

import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KonvertTo(org.tidepool.sdk.model.Association::class, mapFunctionName = "toDomain")
data class AssociationDto(
    @SerialName("type")
    val type: AssociationTypeDto?,
    @SerialName("id")
    val id: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("reason")
    val reason: String?,
) {
    
    @Serializable
    enum class AssociationTypeDto {
        
        @SerialName("blob")
        Blob,
        
        @SerialName("datum")
        Datum,
        
        @SerialName("image")
        Image,
        
        @SerialName("url")
        Url,
        ;
    }
}