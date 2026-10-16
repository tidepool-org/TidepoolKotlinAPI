package org.tidepool.sdk.model.data

/**
 * A datum to remove from Tidepool. [id] drops a copy still waiting in the upload outbox, so it
 * can't go out after its deletion; [originId] selects the copies already uploaded, the way iOS
 * TidepoolService deletes data by origin.
 */
data class DataDeletion(
    val id: String,
    val originId: String,
)
