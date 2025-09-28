package org.tidepool.sdk.model.metadata.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.util.Collections
import java.util.EnumSet

/**
 * Marker class so that deserialization will work properly
 */
@Serializable
open class TrustUser : User() {
    
    @Serializable
    enum class Permission {
        custodian,
        view,
        note,
        upload
    }
    
    @Serializable
    data class JsonPermissions(
        private val custodian: JsonObject? = null,
        private val view: JsonObject? = null,
        private val note: JsonObject? = null,
        private val upload: JsonObject? = null
    ) {
        
        val permissionsSet: Set<Permission> by lazy {
            setOfNotNull(
                Permission.custodian.takeUnless { custodian == null },
                Permission.view.takeUnless { view == null },
                Permission.note.takeUnless { note == null },
                Permission.upload.takeUnless { upload == null },
            )
        }
    }
    
}