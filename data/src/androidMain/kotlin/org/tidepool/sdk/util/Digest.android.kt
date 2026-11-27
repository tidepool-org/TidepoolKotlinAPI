package org.tidepool.sdk.util

import java.security.MessageDigest
import java.util.Base64

// java.util.Base64, not android.util.Base64: the latter returns defaults under host tests.
internal actual fun md5Base64(bytes: ByteArray): String =
    Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(bytes))
