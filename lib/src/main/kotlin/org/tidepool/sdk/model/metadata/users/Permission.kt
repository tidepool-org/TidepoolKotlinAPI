package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.dto.metadata.users.PermissionDto

enum class Permission {
    Custodian,
    View,
    Note,
    Upload,
    ;
}

internal fun PermissionDto.toDomain() = when (this) {
    PermissionDto.Custodian -> Permission.Custodian
    PermissionDto.View      -> Permission.View
    PermissionDto.Note      -> Permission.Note
    PermissionDto.Upload    -> Permission.Upload
}

internal fun Permission.toDto() = when (this) {
    Permission.Custodian -> PermissionDto.Custodian
    Permission.View      -> PermissionDto.View
    Permission.Note      -> PermissionDto.Note
    Permission.Upload    -> PermissionDto.Upload
}