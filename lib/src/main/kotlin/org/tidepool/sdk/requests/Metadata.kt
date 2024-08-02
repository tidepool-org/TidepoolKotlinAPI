package org.tidepool.sdk.requests

import org.tidepool.sdk.model.metadata.users.TrustUser
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.Header

interface Metadata {
	@GET("/metadata/users/{userId}/users")
	suspend fun listUsers(
		@Header("X-Tidepool-Session-Token") sessionToken: String,
		@Path("userId") userId: String
	) : Array<TrustUser>
}