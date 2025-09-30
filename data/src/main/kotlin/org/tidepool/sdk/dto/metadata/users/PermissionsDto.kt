package org.tidepool.sdk.dto.metadata.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
enum class PermissionDto {
    @SerialName("custodian")
    Custodian,
    @SerialName("view")
    View,
    @SerialName("note")
    Note,
    @SerialName("upload")
    Upload,
    ;
}

@Serializable
data class PermissionsDto(
    val custodian: JsonObject? = null,
    val view: JsonObject? = null,
    val note: JsonObject? = null,
    val upload: JsonObject? = null
) {
    
    val permissionsSet: Set<PermissionDto> by lazy {
        setOfNotNull(
            PermissionDto.Custodian.takeUnless { custodian == null },
            PermissionDto.View.takeUnless { view == null },
            PermissionDto.Note.takeUnless { note == null },
            PermissionDto.Upload.takeUnless { upload == null },
        )
    }
}