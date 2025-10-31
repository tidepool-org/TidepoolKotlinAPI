package org.tidepool.sdk.model

public data class Association(
    val type: AssociationType?,
    val id: String?,
    val url: String?,
    val reason: String?,
) {
    
    enum class AssociationType {
        Blob,
        Datum,
        Image,
        Url,
        ;
    }
}