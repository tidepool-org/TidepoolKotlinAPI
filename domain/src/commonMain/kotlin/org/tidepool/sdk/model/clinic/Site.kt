package org.tidepool.sdk.model.clinic

data class Site(
    val id: String,
    val name: String,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    val country: String? = null,
)