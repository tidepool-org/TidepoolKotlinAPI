package org.tidepool.sdk.di

import kotlin.test.Test
import kotlin.test.assertEquals

class HttpLogRedactionTest {

    @Test
    fun `user and data set IDs in a path are replaced`() {
        assertEquals(
            "REQUEST: https://api.tidepool.org/v1/users/***/data_sets",
            redactIdentifiers("REQUEST: https://api.tidepool.org/v1/users/1a2b3c4d5e/data_sets"),
        )
        assertEquals(
            "FROM: https://api.tidepool.org/v1/data_sets/***/data",
            redactIdentifiers("FROM: https://api.tidepool.org/v1/data_sets/0123456789abcdef0123456789abcdef/data"),
        )
    }

    @Test
    fun `an ID before a query or at the end of a line is replaced`() {
        assertEquals(
            "https://api.tidepool.org/v1/users/***?limit=1\nMETHOD: GET /metadata/***",
            redactIdentifiers("https://api.tidepool.org/v1/users/1a2b3c4d5e?limit=1\nMETHOD: GET /metadata/abcdef01234"),
        )
    }

    @Test
    fun `an email in a path is replaced, encoded or not`() {
        assertEquals(
            "https://api.tidepool.org/confirm/forgot/***\nhttps://api.tidepool.org/confirm/resend/signup/***?x=1",
            redactIdentifiers(
                "https://api.tidepool.org/confirm/forgot/person@example.org\n" +
                    "https://api.tidepool.org/confirm/resend/signup/person%40example.org?x=1",
            ),
        )
    }

    @Test
    fun `identifying query values are replaced and the others are kept`() {
        assertEquals(
            "https://api.tidepool.org/v1/data_sets/***/data?uploadId=***&limit=1&deviceId=***\n" +
                "https://api.tidepool.org/v1/clinics?email=***&search=***&name=***&shareCode=***&offset=0",
            redactIdentifiers(
                "https://api.tidepool.org/v1/data_sets/0123456789abcdef0123456789abcdef/data" +
                    "?uploadId=up123&limit=1&deviceId=Pump-9001\n" +
                    "https://api.tidepool.org/v1/clinics" +
                    "?email=person%40example.org&search=Jane&name=Jane%20Doe&shareCode=ABCD-1234&offset=0",
            ),
        )
    }

    @Test
    fun `short segments, words and values outside a path are kept`() {
        val message = "REQUEST: https://api.tidepool.org/v1/data_sets/cafe/data\nBODY: {\"time\": 1700000000000}"

        assertEquals(message, redactIdentifiers(message))
    }
}
