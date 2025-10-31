package org.tidepool.sdk.dto.metadata.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.model.metadata.users.Permission
import org.tidepool.sdk.model.metadata.users.Permission.Custodian
import org.tidepool.sdk.model.metadata.users.Permission.Note
import org.tidepool.sdk.model.metadata.users.Permission.Root
import org.tidepool.sdk.model.metadata.users.Permission.Upload
import org.tidepool.sdk.model.metadata.users.Permission.View

@Serializable
enum class PermissionDto {
    @SerialName("root")
    Root,
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
    @SerialName("custodian")
    val custodian: JsonObject? = null,
    @SerialName("root")
    val root: JsonObject? = null,
    @SerialName("view")
    val view: JsonObject? = null,
    @SerialName("note")
    val note: JsonObject? = null,
    @SerialName("upload")
    val upload: JsonObject? = null,
) {
    
    constructor(permissions: Set<Permission>) : this(
        custodian = permissions.find { it == Custodian }?.let { JsonObject(emptyMap()) },
        root = permissions.find { it == Root }?.let { JsonObject(emptyMap()) },
        view = permissions.find { it == View }?.let { JsonObject(emptyMap()) },
        note = permissions.find { it == Note }?.let { JsonObject(emptyMap()) },
        upload = permissions.find { it == Upload }?.let { JsonObject(emptyMap()) },
    )
    
    val permissionsSet: Set<PermissionDto> by lazy {
        setOfNotNull(
            PermissionDto.Custodian.takeUnless { custodian == null },
            PermissionDto.View.takeUnless { view == null },
            PermissionDto.Note.takeUnless { note == null },
            PermissionDto.Upload.takeUnless { upload == null },
            PermissionDto.Root.takeUnless { root == null },
        )
    }
}

internal fun PermissionDto.toDomain(): Permission = when (this) {
    PermissionDto.Custodian -> Custodian
    PermissionDto.View      -> View
    PermissionDto.Note      -> Note
    PermissionDto.Upload    -> Upload
    PermissionDto.Root      -> Root
}