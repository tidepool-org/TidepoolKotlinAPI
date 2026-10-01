package org.tidepool.sdk

interface TokenProvider {
    /**
     * Whether a session is stored, checked locally without touching the network. Stays true
     * while the access token is expired and can't be refreshed (e.g. offline), so callers can
     * keep work for the session that [getToken] can't serve right now.
     */
    val isLoggedIn: Boolean
    suspend fun getToken(): Result<String>
    fun clearToken()
}
