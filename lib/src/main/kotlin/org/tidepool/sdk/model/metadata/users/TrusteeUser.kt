package org.tidepool.sdk.model.metadata.users

data class TrusteeUser(val trusteePermissions: JsonPermissions = JsonPermissions()) : TrustUser() {
    
    val permissions by trusteePermissions::permissionsSet
}
