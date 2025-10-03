package org.tidepool.sdk.model.metadata.users

import kotlinx.serialization.Serializable

@Serializable
data class TrusteeUser(val trusteePermissions: JsonPermissions = JsonPermissions()) : TrustUser() {
    
    val permissions by trusteePermissions::permissionsSet
}
