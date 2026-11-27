package org.tidepool.sdk.di

/**
 * [message] with the identifiers the SDK's URLs carry replaced by `***`: path segments that are IDs (10 or more hex
 * characters) or email addresses, and the values of the query parameters that name a person, device, upload or share.
 */
internal fun redactIdentifiers(message: String): String = message
    .replace(idSegment, REDACTED)
    .replace(emailSegment, REDACTED)
    .replace(identifyingQueryValue) { it.groupValues[1] + REDACTED }

private const val REDACTED = "***"

private val idSegment = Regex("""(?<=/)[0-9a-fA-F]{10,}(?=[/?#\s]|$)""", RegexOption.MULTILINE)

private val emailSegment = Regex("""(?<=/)[^/?#\s]*(?:@|%40)[^/?#\s]*""")

private val identifyingQueryValue = Regex("""([?&](?:uploadId|deviceId|email|shareCode|search|name)=)[^&#\s]*""")
