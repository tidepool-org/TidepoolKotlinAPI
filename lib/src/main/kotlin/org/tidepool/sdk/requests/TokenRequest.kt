package org.tidepool.sdk.requests;

import com.google.gson.annotations.SerializedName

data class TokenRequest(
	val grant_type: GrantType,
	val client_id: String,
	val client_secret: String? = null,
	val subject_token: String? = null,
	val subject_token_type: SubjectTokenType? = null,
	val requested_token_type: RequestedTokenType? = null,
	val subject_issuer: String? = null,
	val username: String? = null,
	val password: String? = null,
	val code: String? = null,
	val code_verifier: String? = null
) {
	companion object {
		fun createWithAuthorizationCode(client_id: String, code: String) : Builder {
			return Builder(GrantType.authorization_code, client_id).code(code)
		}
		fun createWithRefreshToken(client_id: String) : Builder {
			return Builder(GrantType.refresh_token, client_id)
		}
		fun createWithPassword(client_id: String, username: String, password: String) : Builder {
			return Builder(GrantType.password, client_id).username(username).password(password)
		}
		fun createWithTokenExchange(client_id: String, subject_token: String) : Builder {
			return Builder(GrantType.tokenExchange, client_id).subjectToken(subject_token)
		}
	}
	class Builder(val grant_type: GrantType, val client_id: String) {
		var client_secret: String? = null
		var subject_token: String? = null
		var subject_token_type: SubjectTokenType? = null
		var requested_token_type: RequestedTokenType? = null
		var subject_issuer: String? = null
		var username: String? = null
		var password: String? = null
		var code: String? = null
		var code_verifier: String? = null
		fun clientSecret(client_secret: String) : Builder {
			this.client_secret = client_secret
			return this
		}
		fun subjectToken(subject_token: String) : Builder {
			this.subject_token = subject_token
			return this
		}
		fun subjectTokenType(subject_token_type: SubjectTokenType) : Builder {
			this.subject_token_type = subject_token_type
			return this
		}
		fun requestedTokenType(requested_token_type: RequestedTokenType) : Builder {
			this.requested_token_type = requested_token_type
			return this
		}
		fun subjectIssuer(subject_issuer: String) : Builder {
			this.subject_issuer = subject_issuer
			return this
		}
		fun username(username: String) : Builder {
			this.username = username;
			return this
		}
		fun password(password: String) : Builder {
			this.password = password
			return this
		}
		fun code(code: String) : Builder {
			this.code = code
			return this
		}
		fun codeVerifier(code_verifier: String) : Builder {
			this.code_verifier = code_verifier
			return this
		}
		fun build(): TokenRequest {
			return TokenRequest(
				grant_type,
				client_id,
				client_secret,
				subject_token,
				subject_token_type,
				requested_token_type,
				subject_issuer,
				username,
				password,
				code,
				code_verifier
			)
		}
	}
}

enum class GrantType {
	authorization_code,
	refresh_token,
	password,
	@SerializedName("urn:ietf:params:oauth:grant-type:token-exchange")
	tokenExchange
}

enum class SubjectTokenType {
	@SerializedName("urn:ietf:params:oauth:token-type:access_token")
	access_token,
	@SerializedName("urn:inetf:params:oauth:token-type:jwt")
	jwt
}

enum class RequestedTokenType {
	@SerializedName("urn:ietf:params:oauth:token-type:access_token")
	access_token,
	@SerializedName("urn:ietf:params:oauth:token-type:refresh_token")
	refresh_token
}