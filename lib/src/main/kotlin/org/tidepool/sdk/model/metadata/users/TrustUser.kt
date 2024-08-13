package org.tidepool.sdk.model.metadata.users

import com.google.gson.JsonObject
import java.util.Collections
import java.util.EnumSet

/**
 * Marker class so that deserialization will work properly
 */
open class TrustUser : User() {
    
    enum class Permission {
        custodian,
        view,
        note,
        upload
    }
    
    data class JsonPermissions(
        private val custodian: JsonObject? = null,
        private val view: JsonObject? = null,
        private val note: JsonObject? = null,
        private val upload: JsonObject? = null
    ) {
        
        val permissionsSet: Set<Permission> by lazy {
            val permissions = EnumSet.noneOf(Permission::class.java)
            if (custodian != null) {
                permissions.add(Permission.custodian)
            }
            if (view != null) {
                permissions.add(Permission.view)
            }
            if (note != null) {
                permissions.add(Permission.note)
            }
            if (upload != null) {
                permissions.add(Permission.upload)
            }
            Collections.unmodifiableSet(permissions)
        }
    }
    
}