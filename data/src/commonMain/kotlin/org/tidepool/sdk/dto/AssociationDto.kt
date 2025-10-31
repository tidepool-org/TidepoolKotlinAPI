package org.tidepool.sdk.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.Association

@Serializable
data class AssociationDto(
    @SerialName("type")
    val type: AssociationTypeDto?,
    
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

fun AssociationDto.toDomain(): Association = Association(
    type = type?.toDomain(),
    id = id,
    url = url,
    reason = reason,
)

fun Association.toDto(): AssociationDto = AssociationDto(
    type = type?.toDto(),
    id = id,
    url = url,
    reason = reason,
)

fun AssociationDto.AssociationTypeDto.toDomain(): Association.AssociationType = when (this) {
    AssociationDto.AssociationTypeDto.Blob -> Association.AssociationType.Blob
    AssociationDto.AssociationTypeDto.Datum -> Association.AssociationType.Datum
    AssociationDto.AssociationTypeDto.Image -> Association.AssociationType.Image
    AssociationDto.AssociationTypeDto.Url -> Association.AssociationType.Url
}

fun Association.AssociationType.toDto(): AssociationDto.AssociationTypeDto = when (this) {
    Association.AssociationType.Blob -> AssociationDto.AssociationTypeDto.Blob
    Association.AssociationType.Datum -> AssociationDto.AssociationTypeDto.Datum
    Association.AssociationType.Image -> AssociationDto.AssociationTypeDto.Image
    Association.AssociationType.Url -> AssociationDto.AssociationTypeDto.Url
}