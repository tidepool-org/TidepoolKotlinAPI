package org.tidepool.sdk.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.auth.Realm

@Serializable
enum class RealmDto {
    
    @SerialName("dev1")
    Dev1,
    
    @SerialName("qa1")
    Qa1,
    
    @SerialName("qa2")
    Qa2,
    
    @SerialName("qa3")
    Qa3,
    
    @SerialName("qa4")
    Qa4,
    
    @SerialName("qa5")
    Qa5,
    
    @SerialName("integration")
    Integration,
    
    @SerialName("tidepool")
    Tidepool
}

internal fun Realm.toDto() = when (this) {
    Realm.Dev1        -> RealmDto.Dev1
    Realm.Qa1         -> RealmDto.Qa1
    Realm.Qa2         -> RealmDto.Qa2
    Realm.Qa3         -> RealmDto.Qa3
    Realm.Qa4         -> RealmDto.Qa4
    Realm.Qa5         -> RealmDto.Qa5
    Realm.Integration -> RealmDto.Integration
    Realm.Tidepool    -> RealmDto.Tidepool
}