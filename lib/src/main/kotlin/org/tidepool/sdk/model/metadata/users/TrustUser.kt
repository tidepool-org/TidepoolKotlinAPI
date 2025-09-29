package org.tidepool.sdk.model.metadata.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
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
        
        @SerialName("custodian")
        Custodian,
        
        @SerialName("view")
        View,
        
        @SerialName("note")
        Note,
        
        @SerialName("upload")
        Upload
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
                Permission.Custodian.takeUnless { custodian == null },
                Permission.View.takeUnless { view == null },
                Permission.Note.takeUnless { note == null },
                Permission.Upload.takeUnless { upload == null },
            )
        }
    }
    
}