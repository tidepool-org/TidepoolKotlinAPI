package org.tidepool.sdk.model.metadata

enum class MetadataCollection(val code: String) {
    Profile("profile"),
    Preferences("preferences"),
    Settings("settings"),
    Groups("groups"),
    Private("private"),
    ;
}