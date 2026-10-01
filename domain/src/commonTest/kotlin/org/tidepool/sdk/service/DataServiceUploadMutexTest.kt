package org.tidepool.sdk.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.repository.DataRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Verifies that [DataService.uploadCachedDataNow] serializes concurrent callers, so the
 * foreground lifecycle-aware loop and a background caller (e.g. a WorkManager worker) can never
 * race on token fetch, data set creation, and the upload itself, and that it drains the outbox
 * batch by batch within its iteration cap.
 */
class DataServiceUploadMutexTest {

    @Test
    fun concurrentUploadCachedDataNowCallsNeverOverlap() = runTest {
        val dataRepository = RecordingDataRepository()
        val dataService = DataService(
            dataRepository = dataRepository,
            userRepository = FakeUserRepository(),
            tokenProvider = FakeTokenProvider(),
        )

        val results = listOf(
            async { dataService.uploadCachedDataNow() },
            async { dataService.uploadCachedDataNow() },
        ).awaitAll()

        assertTrue(results.all { it.isSuccess })
        assertEquals(1, dataRepository.maxObservedConcurrency)
        assertEquals(2, dataRepository.completedCallCount)
    }

    /**
     * Regression test: [DataService.uploadCachedDataNow] must keep the token/data set/upload
     * chain inside [Result] (via `flatMap`) instead of calling `getOrThrow()` on an intermediate
     * result. A `getOrThrow()` there would let a transient failure escape as a throw instead of
     * a `Result.failure`, which would make the caller (a WorkManager worker) record a hard
     * failure instead of retrying.
     */
    @Test
    fun tokenFailureIsReturnedAsResultFailureNotThrown() = runTest {
        val expected = IllegalStateException("getToken failed")
        val dataService = DataService(
            dataRepository = RecordingDataRepository(),
            userRepository = FakeUserRepository(),
            tokenProvider = FailingTokenProvider(expected),
        )

        val result = dataService.uploadCachedDataNow()

        assertTrue(result.isFailure)
        assertEquals(expected, result.exceptionOrNull())
    }

    @Test
    fun drainsAgainWhileBatchIsFull() = runTest {
        val dataRepository = RecordingDataRepository(
            scriptedResults = listOf(Result.success(true), Result.success(true), Result.success(false)),
        )

        val result = dataServiceWith(dataRepository).uploadCachedDataNow()

        assertTrue(result.isSuccess)
        assertEquals(3, dataRepository.completedCallCount)
    }

    @Test
    fun stopsAfterMaxDrainIterationsWhenEveryBatchIsFull() = runTest {
        val dataRepository = RecordingDataRepository(fallbackResult = Result.success(true))

        val result = dataServiceWith(dataRepository).uploadCachedDataNow()

        assertTrue(result.isSuccess)
        assertEquals(10, dataRepository.completedCallCount)
    }

    @Test
    fun stopsOnFirstFailureAfterEarlierSuccess() = runTest {
        val expected = IllegalStateException("upload failed")
        val dataRepository = RecordingDataRepository(
            scriptedResults = listOf(Result.success(true), Result.failure(expected)),
            fallbackResult = Result.success(true),
        )

        val result = dataServiceWith(dataRepository).uploadCachedDataNow()

        assertTrue(result.isFailure)
        assertEquals(expected, result.exceptionOrNull())
        assertEquals(2, dataRepository.completedCallCount)
    }

    private fun dataServiceWith(dataRepository: DataRepository) = DataService(
        dataRepository = dataRepository,
        userRepository = FakeUserRepository(),
        tokenProvider = FakeTokenProvider(),
    )
}
