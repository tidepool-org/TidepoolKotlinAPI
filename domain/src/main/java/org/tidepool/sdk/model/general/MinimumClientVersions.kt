package org.tidepool.sdk.model.general

data class MinimumClientVersions(
    val versions: Versions
) {
    
    data class Versions(
        val uploaderMinimum: String
    )
}