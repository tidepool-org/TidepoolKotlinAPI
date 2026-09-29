package org.tidepool.sdk.fake

import kotlinx.serialization.decodeFromString
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.dto.ResponseDto
import org.tidepool.sdk.dto.data.BaseDataDto
import org.tidepool.sdk.dto.data.DataSetDto
import org.tidepool.sdk.dto.user.UserDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Each canned body must decode into the DTO the real API client expects, with the same Json
 * config, or the fake backend would fail in ways the real one never does.
 */
class FakeBackendResponsesTest {

    private val json = createTidepoolJson()

    @Test
    fun `current user decodes with the fake user id`() {
        val response = respond("GET", "auth/user")

        assertEquals(200, response.status)
        val user = json.decodeFromString<UserDto>(response.body)
        assertEquals(FakeBackendResponses.USER_ID, user.userId)
        assertEquals(FakeBackendResponses.USERNAME, user.username)
    }

    @Test
    fun `data set list is empty so each session creates one`() {
        val response = respond("GET", "v1/users/fake-user/data_sets")

        assertEquals(200, response.status)
        assertTrue(json.decodeFromString<List<DataSetDto>>(response.body).isEmpty())
    }

    @Test
    fun `created data set has an id`() {
        val response = respond("POST", "v1/users/fake-user/data_sets")

        assertEquals(201, response.status)
        assertEquals(FakeBackendResponses.DATA_SET_ID, json.decodeFromString<DataSetDto>(response.body).id)
    }

    @Test
    fun `data upload decodes as an empty response`() {
        val response = respond("POST", "v1/data_sets/fake-data-set/data")

        assertEquals(200, response.status)
        val decoded = json.decodeFromString<ResponseDto<BaseDataDto>>(response.body)
        assertTrue(decoded.data.isEmpty())
        assertTrue(decoded.errors.isEmpty())
    }

    @Test
    fun `data set deletes succeed`() {
        assertEquals(200, respond("DELETE", "v1/data_sets/fake-data-set").status)
        assertEquals(200, respond("DELETE", "v1/data_sets/fake-data-set/data").status)
    }

    @Test
    fun `prescription list is empty`() {
        val response = respond("GET", "v1/patients/fake-user/prescriptions")

        assertEquals(200, response.status)
        assertEquals("[]", response.body)
    }

    @Test
    fun `unknown request gets a JSON 404`() {
        val response = respond("GET", "v1/users/fake-user/data_sources")

        assertEquals(404, response.status)
        assertTrue(response.body.startsWith("{"))
    }

    @Test
    fun `method must match too`() {
        assertEquals(404, respond("POST", "auth/user").status)
    }

    private fun respond(method: String, path: String) =
        FakeBackendResponses.respond(method = method, pathSegments = path.split("/"))
}
