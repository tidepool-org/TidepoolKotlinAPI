package org.tidepool.sdk.dto.metadata.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.model.metadata.users.Permission
import org.tidepool.sdk.model.metadata.users.Permission.Custodian
import org.tidepool.sdk.model.metadata.users.Permission.Note
import org.tidepool.sdk.model.metadata.users.Permission.Upload
import org.tidepool.sdk.model.metadata.users.Permission.View

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

internal fun PermissionDto.toDomain(): Permission = when (this) {
    PermissionDto.Custodian -> Custodian
    PermissionDto.View      -> View
    PermissionDto.Note      -> Note
    PermissionDto.Upload    -> Upload
}