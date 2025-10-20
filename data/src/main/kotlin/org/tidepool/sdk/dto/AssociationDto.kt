package org.tidepool.sdk.dto

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.Association

@Serializable
@KonvertTo(Association::class, mapFunctionName = "toDomain")
data class AssociationDto(
    @SerialName("type")
    val type: AssociationTypeDto?,
    
    val id: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("reason")
    val reason: String?,
) {
    
    @KonvertFrom(Association::class, mapFunctionName = "fromDomain")
    companion object {}
    
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