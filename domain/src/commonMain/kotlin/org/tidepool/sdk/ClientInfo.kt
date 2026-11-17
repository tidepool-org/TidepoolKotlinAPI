package org.tidepool.sdk

/**
 * Identifies this client to the platform. Sent as the `client` field of every data set this SDK
 * creates, and used to recognise a data set this client (rather than another client, or a deleted
 * one) created earlier, the way iOS's `TidepoolService` does.
 */
data class ClientInfo(
    val name: String,
    val version: String,
)
