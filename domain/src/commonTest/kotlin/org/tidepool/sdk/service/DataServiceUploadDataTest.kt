package org.tidepool.sdk.service

import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.UnauthorizedException
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Verifies that [DataService.uploadData] caches data for the next upload whenever a session is
 * stored, whether or not a token can be fetched right now, and caches nothing when logged out.
 */
class DataServiceUploadDataTest {

    private val sample: BaseData = ContinuousGlucoseData(id = "cgm-1")

    /**
     * Regression test: caching used to wait on [TokenProvider.getToken], which needs the network
     * once the access token expires, so data recorded offline after expiry was dropped instead
     * of cached for the upload backstop.
     */
    @Test
    fun cachesDataWhileLoggedInEvenWhenTokenFetchFails() = runTest {
        val dataRepository = RecordingDataRepository()
        val dataService = dataServiceWith(
            dataRepository = dataRepository,
            tokenProvider = FailingTokenProvider(IllegalStateException("offline")),
        )

        val result = dataService.uploadData(sample)

        assertTrue(result.isSuccess)
        assertEquals(listOf(sample), dataRepository.cachedData)
    }

    @Test
    fun cachesNothingWhenLoggedOut() = runTest {
        val dataRepository = RecordingDataRepository()
        val dataService = dataServiceWith(
            dataRepository = dataRepository,
            tokenProvider = FakeTokenProvider(isLoggedIn = false),
        )

        val result = dataService.uploadData(sample)

        assertIs<UnauthorizedException>(result.exceptionOrNull())
        assertTrue(dataRepository.cachedData.isEmpty())
    }

    private fun dataServiceWith(
        dataRepository: RecordingDataRepository,
        tokenProvider: TokenProvider,
    ) = DataService(
        dataRepository = dataRepository,
        userRepository = FakeUserRepository(),
        tokenProvider = tokenProvider,
    )
}
