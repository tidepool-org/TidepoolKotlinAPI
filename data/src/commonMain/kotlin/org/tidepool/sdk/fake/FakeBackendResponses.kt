package org.tidepool.sdk.fake

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal data class FakeResponse(
    val status: Int,
    val reason: String,
    val body: String,
)

/**
 * Canned answers for the fake backend: just enough for the calls the app makes to succeed.
 * Stateless, so every session creates its data set again, which also records that request.
 */
internal object FakeBackendResponses {

    const val USER_ID = "fake-user"
    const val USERNAME = "fake@tidepool.invalid"
    const val DATA_SET_ID = "fake-data-set"

    private const val ANY = "*"

    fun respond(method: String, pathSegments: List<String>): FakeResponse {
        val path = pathSegments.filter { it.isNotEmpty() }
        return when {
            method == "GET" && (path.matches("auth", "user") || path.matches("auth", "user", ANY)) ->
                ok(
                    buildJsonObject {
                        put("userid", USER_ID)
                        put("username", USERNAME)
                    }
                )

            method == "GET" && path.matches("v1", "users", ANY, "data_sets") -> ok(JsonArray(emptyList()))

            method == "POST" && path.matches("v1", "users", ANY, "data_sets") -> response(
                status = 201,
                reason = "Created",
                body = buildJsonObject {
                    put("id", DATA_SET_ID)
                    put("uploadId", DATA_SET_ID)
                },
            )

            method == "POST" && path.matches("v1", "data_sets", ANY, "data") -> ok(JsonObject(emptyMap()))

            method == "DELETE" && path.size >= 3 && path[0] == "v1" && path[1] == "data_sets" ->
                ok(JsonObject(emptyMap()))

            method == "GET" && path.matches("v1", "patients", ANY, "prescriptions") -> ok(JsonArray(emptyList()))

            else -> response(
                status = 404,
                reason = "Not Found",
                body = buildJsonObject {
                    put("code", "not_found")
                    put("message", "The fake backend has no response for $method /${path.joinToString("/")}")
                },
            )
        }
    }

    private fun ok(body: JsonElement) = response(status = 200, reason = "OK", body = body)

    private fun response(status: Int, reason: String, body: JsonElement) =
        FakeResponse(status = status, reason = reason, body = body.toString())

    private fun List<String>.matches(vararg pattern: String) =
        size == pattern.size && zip(pattern).all { (segment, expected) -> expected == ANY || segment == expected }
}
