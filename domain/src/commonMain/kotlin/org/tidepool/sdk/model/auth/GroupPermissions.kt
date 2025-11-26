package org.tidepool.sdk.model.auth

import org.tidepool.sdk.model.metadata.users.Permission

/**
 * Access permissions granted to one or more users within a group.
 * The key for each entry is the Tidepool User ID of that user.
 */
data class GroupPermissions(
    val userPermissions: Map<String, Set<Permission>>
)