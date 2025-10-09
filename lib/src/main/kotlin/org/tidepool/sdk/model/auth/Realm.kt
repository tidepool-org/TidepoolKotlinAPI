package org.tidepool.sdk.model.auth

import org.tidepool.sdk.dto.auth.RealmDto

enum class Realm {
    Dev1,
    Qa1,
    Qa2,
    Qa3,
    Qa4,
    Qa5,
    Integration,
    Tidepool,
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