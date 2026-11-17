package org.tidepool.sdk.service

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.tidepool.sdk.ClientInfo
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.DataDeletion
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.repository.DataRepository
import kotlin.time.Instant
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Regression test for a race between [DataService.clearCachedData] and a data set creation
 * already in flight inside [DataService.uploadCachedDataNow]: [RaceyDataSetRepository] mirrors
 * DataRepositoryImpl's real `awaitOrCreateCachedDataSetId`/cachedDataSetId contract (a creator
 * persists the id unconditionally once `create()` resolves). `clearCachedData()` takes the same
 * upload mutex an in-flight batch holds while awaiting the data set id, so a creation that was
 * already running when clear was called can never finish afterwards and resurrect the cleared,
 * now-stale id.
 */
class DataServiceClearCachedDataRaceTest {

    @Test
    fun `a data set creation finishing after a concurrent clear doesn't resurrect the old id`() = runTest {
        val repository = RaceyDataSetRepository()
        val dataService = DataService(
            dataRepository = repository,
            userRepository = FakeUserRepository(),
            tokenProvider = FakeTokenProvider(),
            clientInfo = ClientInfo(name = "org.tidepool.loop", version = "TEST"),
        )

        val upload = async { dataService.uploadCachedDataNow() }
        repository.creationStarted.await()

        val clear = async { dataService.clearCachedData() }
        // Let clearCachedData() queue behind uploadMutex, which the upload batch already holds.
        yield()
        repository.resumeCreation.complete(Unit)

        assertTrue(upload.await().isSuccess)
        assertTrue(clear.await().isSuccess)
        assertNull(repository.cachedId)
    }
}

/**
 * A [DataRepository] whose [awaitOrCreateCachedDataSetId] copies DataRepositoryImpl's real
 * CompletableDeferred dance over [cachedId], standing in for the real cachedDataSetId backed by
 * KEY_CACHED_DATA_SET_ID_V2. [getUserDataSets] suspends on [resumeCreation] so a test can hold a
 * creation in flight, then always reports no existing data set, so the chain falls through to
 * [createDataSet].
 */
private class RaceyDataSetRepository : DataRepository {

    var cachedId: String? = null
        private set

    val creationStarted = CompletableDeferred<Unit>()
    val resumeCreation = CompletableDeferred<Unit>()

    private val dataSetIdMutex = Mutex()
    private var dataSetIdDeferred: CompletableDeferred<String>? = null

    override suspend fun awaitOrCreateCachedDataSetId(
        create: suspend () -> Result<String>,
    ): Result<String> {
        cachedId?.let { return Result.success(it) }

        var isCreator = false
        val localDeferred = dataSetIdMutex.withLock {
            cachedId?.let { return Result.success(it) }
            if (dataSetIdDeferred == null) {
                dataSetIdDeferred = CompletableDeferred()
                isCreator = true
            }
            dataSetIdDeferred!!
        }

        if (isCreator) {
            val result = create()
            dataSetIdMutex.withLock {
                if (result.isSuccess) {
                    cachedId = result.getOrThrow()
                    dataSetIdDeferred?.complete(result.getOrThrow())
                } else {
                    val throwable = result.exceptionOrNull() ?: IllegalStateException("Unknown error")
                    dataSetIdDeferred?.completeExceptionally(throwable)
                }
                dataSetIdDeferred = null
            }
            return result
        }

        return try {
            Result.success(localDeferred.await())
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getUserDataSets(
        userId: String,
        sessionToken: String,
        page: Int?,
        size: Int?,
        clientName: String?,
        deleted: Boolean?,
    ): Result<List<DataSet>> {
        creationStarted.complete(Unit)
        resumeCreation.await()
        return Result.success(emptyList())
    }

    override suspend fun createDataSet(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String,
    ): Result<DataSet> = Result.success(DataSet(id = "stale-id", client = newDataSet.client))

    override suspend fun uploadCachedData(sessionToken: String, dataSetId: String): Result<Boolean> =
        Result.success(false)

    override suspend fun clearCachedData(): Result<Unit> {
        cachedId = null
        return Result.success(Unit)
    }

    override fun clearCachedDataSetId() {
        cachedId = null
    }

    override suspend fun hasCachedData(): Boolean = false

    override suspend fun cacheDataDeletions(deletions: List<DataDeletion>) = Unit

    override suspend fun uploadDataToDataSet(data: List<BaseData>): Result<List<BaseData>> = notImplemented()

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

    override suspend fun getDataSet(dataSetId: String, sessionToken: String): Result<DataSet> = notImplemented()

    override suspend fun updateDataSet(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String,
    ): Result<DataSet> = notImplemented()

    override suspend fun deleteDataSet(dataSetId: String, sessionToken: String): Result<Unit> = notImplemented()

    override suspend fun deleteDataSetData(dataSetId: String, sessionToken: String): Result<Unit> = notImplemented()

    override suspend fun getUserDataSetsLegacy(userId: String, sessionToken: String): Result<List<DataSet>> =
        notImplemented()

    override suspend fun createDataSetLegacy(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String,
    ): Result<DataSet> = notImplemented()

    override suspend fun getDataSetLegacy(dataSetId: String, sessionToken: String): Result<DataSet> = notImplemented()

    override suspend fun updateDataSetLegacy(
        dataSetId: String,
        dataSet: DataSet,
        sessionToken: String,
    ): Result<DataSet> = notImplemented()

    override suspend fun deleteDataSetLegacy(dataSetId: String, sessionToken: String): Result<Unit> = notImplemented()

    override suspend fun uploadDataToDataSetLegacy(
        dataSetId: String,
        data: List<BaseData>,
        sessionToken: String,
    ): Result<List<BaseData>> = notImplemented()

    override suspend fun getUserDataSources(userId: String, sessionToken: String): Result<List<DataSource>> =
        notImplemented()

    override suspend fun createDataSource(
        userId: String,
        newDataSource: NewDataSource,
        sessionToken: String,
    ): Result<DataSource> = notImplemented()

    override suspend fun deleteAllDataSources(userId: String, sessionToken: String): Result<Unit> = notImplemented()

    override suspend fun getDataSource(dataSourceId: String, sessionToken: String): Result<DataSource> =
        notImplemented()

    override suspend fun updateDataSource(
        dataSourceId: String,
        dataSource: DataSource,
        sessionToken: String,
    ): Result<DataSource> = notImplemented()

    override suspend fun deleteDataSource(dataSourceId: String, sessionToken: String): Result<Unit> = notImplemented()

    override suspend fun deleteAllUserData(userId: String, sessionToken: String): Result<Unit> = notImplemented()

    private fun notImplemented(): Nothing = throw NotImplementedError()
}
