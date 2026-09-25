package org.tidepool.sdk.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.model.metadata.users.User
import org.tidepool.sdk.repository.DataRepository
import org.tidepool.sdk.repository.UserRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Verifies that [DataService.uploadCachedDataNow] serializes concurrent callers, so the
 * foreground lifecycle-aware loop and a background caller (e.g. a WorkManager worker) can never
 * race on token fetch, data set creation, and the upload itself.
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

    /** Records how many calls to [uploadCachedData] are in flight at once. */
    private class RecordingDataRepository : DataRepository {

        var maxObservedConcurrency = 0
            private set
        var completedCallCount = 0
            private set

        private var activeCallCount = 0

        override suspend fun uploadCachedData(
            userId: String,
            sessionToken: String,
            dataSetId: String,
        ): Result<Unit> {
            activeCallCount++
            maxObservedConcurrency = maxOf(maxObservedConcurrency, activeCallCount)
            delay(50)
            activeCallCount--
            completedCallCount++
            return Result.success(Unit)
        }

        override suspend fun awaitOrCreateCachedDataSetId(
            create: suspend () -> Result<String>,
        ): Result<String> = Result.success("dataset-1")

        override fun clearCachedDataSetId() = Unit

        override suspend fun getDataForUser(
            userId: String,
            uploadId: String?,
            deviceId: String?,
            types: List<DataType>?,
            startDate: Instant?,
            endDate: Instant?,
            latest: Boolean?,
            dexcom: Boolean?,
            carelink: Boolean?,
            medtronic: Boolean?,
            sessionToken: String,
        ): Result<List<BaseData>> = notImplemented()

        override suspend fun getUserDataSets(
            userId: String,
            sessionToken: String,
            page: Int?,
            size: Int?,
        ): Result<List<DataSet>> = notImplemented()

        override suspend fun createDataSet(
            userId: String,
            newDataSet: NewDataSet,
            sessionToken: String,
        ): Result<DataSet> = notImplemented()

        override suspend fun getDataSet(
            dataSetId: String,
            sessionToken: String,
        ): Result<DataSet> = notImplemented()

        override suspend fun updateDataSet(
            dataSetId: String,
            dataSet: DataSet,
            sessionToken: String,
        ): Result<DataSet> = notImplemented()

        override suspend fun deleteDataSet(
            dataSetId: String,
            sessionToken: String,
        ): Result<Unit> = notImplemented()

        override suspend fun uploadDataToDataSet(
            data: List<BaseData>,
            sessionToken: String,
        ): Result<List<BaseData>> = notImplemented()

        override suspend fun deleteDataSetData(
            dataSetId: String,
            sessionToken: String,
        ): Result<Unit> = notImplemented()

        override suspend fun getUserDataSetsLegacy(
            userId: String,
            sessionToken: String,
        ): Result<List<DataSet>> = notImplemented()

        override suspend fun createDataSetLegacy(
            userId: String,
            newDataSet: NewDataSet,
            sessionToken: String,
        ): Result<DataSet> = notImplemented()

        override suspend fun getDataSetLegacy(
            dataSetId: String,
            sessionToken: String,
        ): Result<DataSet> = notImplemented()

        override suspend fun updateDataSetLegacy(
            dataSetId: String,
            dataSet: DataSet,
            sessionToken: String,
        ): Result<DataSet> = notImplemented()

        override suspend fun deleteDataSetLegacy(
            dataSetId: String,
            sessionToken: String,
        ): Result<Unit> = notImplemented()

        override suspend fun uploadDataToDataSetLegacy(
            dataSetId: String,
            data: List<BaseData>,
            sessionToken: String,
        ): Result<List<BaseData>> = notImplemented()

        override suspend fun getUserDataSources(
            userId: String,
            sessionToken: String,
        ): Result<List<DataSource>> = notImplemented()

        override suspend fun createDataSource(
            userId: String,
            newDataSource: NewDataSource,
            sessionToken: String,
        ): Result<DataSource> = notImplemented()

        override suspend fun deleteAllDataSources(
            userId: String,
            sessionToken: String,
        ): Result<Unit> = notImplemented()

        override suspend fun getDataSource(
            dataSourceId: String,
            sessionToken: String,
        ): Result<DataSource> = notImplemented()

        override suspend fun updateDataSource(
            dataSourceId: String,
            dataSource: DataSource,
            sessionToken: String,
        ): Result<DataSource> = notImplemented()

        override suspend fun deleteDataSource(
            dataSourceId: String,
            sessionToken: String,
        ): Result<Unit> = notImplemented()

        override suspend fun deleteAllUserData(
            userId: String,
            sessionToken: String,
        ): Result<Unit> = notImplemented()

        private fun notImplemented(): Nothing = throw NotImplementedError()
    }

    private class FakeTokenProvider : TokenProvider {
        override suspend fun getToken(): Result<String> = Result.success("token")
        override fun clearToken() = Unit
    }

    private class FakeUserRepository : UserRepository {
        override suspend fun getCurrentUser(sessionToken: String): Result<User> =
            Result.success(User(userId = "user-1"))

        override suspend fun getUser(userId: String, sessionToken: String): Result<User> =
            throw NotImplementedError()
    }
}
