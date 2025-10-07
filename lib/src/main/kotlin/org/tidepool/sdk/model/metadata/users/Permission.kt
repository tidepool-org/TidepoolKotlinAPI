package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.dto.metadata.users.PermissionDto

enum class Permission {
    Root,
    Custodian,
    View,
    Note,
    Upload,
    ;
    
    val value: String
        get() = when (this) {
            Root      -> "root"
            Custodian -> "custodian"
            View      -> "view"
            Note      -> "note"
            Upload    -> "upload"
        }
}

internal fun PermissionDto.toDomain() = when (this) {
    PermissionDto.Root      -> Permission.Root
    PermissionDto.Custodian -> Permission.Custodian
    PermissionDto.View      -> Permission.View
    PermissionDto.Note      -> Permission.Note
    PermissionDto.Upload    -> Permission.Upload
}

internal fun Permission.toDto() = when (this) {
    Permission.Root      -> PermissionDto.Root
    Permission.Custodian -> PermissionDto.Custodian
    Permission.View      -> PermissionDto.View
    Permission.Note      -> PermissionDto.Note
    Permission.Upload    -> PermissionDto.Upload
}