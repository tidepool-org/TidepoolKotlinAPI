package org.tidepool.sdk.model.data

/**
 * Where a datum came from. Uploading a datum with the same origin id replaces the earlier one (data
 * sets deduplicate by origin), and a queued deletion selects the datum by it.
 */
data class Origin(
    val id: String,
    val name: String,
    val type: String,
    val version: String,
)
