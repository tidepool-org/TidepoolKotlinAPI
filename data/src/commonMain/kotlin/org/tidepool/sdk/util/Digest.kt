package org.tidepool.sdk.util

/** Base64 of the MD5 of [bytes], the value of an HTTP `Digest: MD5=...` header. */
internal expect fun md5Base64(bytes: ByteArray): String
