package org.tidepool.sdk.service

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.UnauthorizedException
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import org.tidepool.sdk.model.data.DataDeletion
import org.tidepool.sdk.ClientInfo

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

    // A deleted datum's copy still waiting in the outbox must be dropped now, or it would go out
    // after the next login with nothing deleting it.
    @Test
    fun queuesDeletionsWhenLoggedOut() = runTest {
        val dataRepository = RecordingDataRepository()
        val dataService = dataServiceWith(
            dataRepository = dataRepository,
            tokenProvider = FakeTokenProvider(isLoggedIn = false),
        )
        val deletion = DataDeletion(id = "food-1", originId = "sync-1")

        val result = dataService.deleteData(listOf(deletion))

        assertTrue(result.isSuccess)
        assertEquals(listOf(deletion), dataRepository.cachedDeletions)
    }

    @Test
    fun signalsWhenDataIsQueued() = runTest {
        val dataService = dataServiceWith(RecordingDataRepository(), FakeTokenProvider())
        val signals = countDataQueued(dataService)

        dataService.uploadData(sample)

        assertEquals(1, signals())
    }

    @Test
    fun signalsWhenADeletionIsQueued() = runTest {
        val dataService = dataServiceWith(RecordingDataRepository(), FakeTokenProvider())
        val signals = countDataQueued(dataService)

        dataService.deleteData(listOf(DataDeletion(id = "food-1", originId = "sync-1")))

        assertEquals(1, signals())
    }

    @Test
    fun noSignalWhenNothingIsQueued() = runTest {
        val dataService = dataServiceWith(RecordingDataRepository(), FakeTokenProvider(isLoggedIn = false))
        val signals = countDataQueued(dataService)

        dataService.uploadData(sample)
        dataService.deleteData(emptyList())

        assertEquals(0, signals())
    }

    private fun TestScope.countDataQueued(dataService: DataService): () -> Int {
        var count = 0
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            dataService.dataQueued.collect { count++ }
        }
        return { count }
    }

    private fun dataServiceWith(
        dataRepository: RecordingDataRepository,
        tokenProvider: TokenProvider,
    ) = DataService(
        dataRepository = dataRepository,
        userRepository = FakeUserRepository(),
        tokenProvider = tokenProvider,
        clientInfo = ClientInfo(name = "org.tidepool.loop", version = "TEST"),
    )
}
