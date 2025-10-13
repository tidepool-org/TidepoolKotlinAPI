package org.tidepool.sdk.model.metadata.users

import org.tidepool.sdk.dto.metadata.users.PermissionDto

enum class Permission {
    Custodian,
    View,
    Note,
    Upload,
    ;
    
    companion object {
        internal fun fromDto(dto: PermissionDto) = when (dto) {
            PermissionDto.Custodian -> Custodian
            PermissionDto.View      -> View
            PermissionDto.Note      -> Note
            PermissionDto.Upload    -> Upload
        }
    }
}