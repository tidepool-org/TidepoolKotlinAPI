package org.tidepool.sdk.model.metadata.users

import kotlinx.serialization.Serializable

@Serializable
data class TrustorUser(private val trustorPermissions: JsonPermissions = JsonPermissions()) :
    TrustUser() {
    
    val permissions by trustorPermissions::permissionsSet
}
