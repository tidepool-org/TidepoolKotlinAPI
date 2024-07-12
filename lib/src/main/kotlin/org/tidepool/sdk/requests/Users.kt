package org.tidepool.sdk.requests

import retrofit2.http.GET
import retrofit2.http.Header
import org.tidepool.sdk.model.auth.User

interface Users {
	@GET("/auth/user")
	fun getCurrentUserInfo(
		@Header("X-Tidepool-Session-Token") sessionToken: String
	) : User
}