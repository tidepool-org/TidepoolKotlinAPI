package org.tidepool.sdk.model.metadata.users

data class TrustorUser(private val trustorPermissions: JsonPermissions = JsonPermissions()) :
    TrustUser() {
    
    val permissions by trustorPermissions::permissionsSet
}
