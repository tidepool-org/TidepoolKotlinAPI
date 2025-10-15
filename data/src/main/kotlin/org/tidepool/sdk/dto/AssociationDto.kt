package org.tidepool.sdk.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssociationDto(
    @SerialName("type")
    val type: AssociationTypeDto?,
    @SerialName("id")
    val id: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("reason")
    val reason: String?
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