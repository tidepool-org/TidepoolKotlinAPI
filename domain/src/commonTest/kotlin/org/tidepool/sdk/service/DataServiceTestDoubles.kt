package org.tidepool.sdk.service

import kotlinx.coroutines.delay
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

/**
 * Records how many calls to [uploadCachedData] are in flight at once, and returns
 * [scriptedResults] in order, then [fallbackResult] for every further call. Data passed to
 * [uploadDataToDataSet] is kept in [cachedData].
 */
internal class RecordingDataRepository(
    scriptedResults: List<Result<Boolean>> = emptyList(),
    private val fallbackResult: Result<Boolean> = Result.success(false),
) : DataRepository {

    var maxObservedConcurrency = 0
        private set
    var completedCallCount = 0
        private set
    val cachedData = mutableListOf<BaseData>()

    private var activeCallCount = 0
    private val remainingResults = ArrayDeque(scriptedResults)

    override suspend fun uploadCachedData(
        sessionToken: String,
        dataSetId: String,
    ): Result<Boolean> {
        activeCallCount++
        maxObservedConcurrency = maxOf(maxObservedConcurrency, activeCallCount)
        delay(50)
        activeCallCount--
        completedCallCount++
        return remainingResults.removeFirstOrNull() ?: fallbackResult
    }

    override suspend fun awaitOrCreateCachedDataSetId(
        create: suspend () -> Result<String>,
    ): Result<String> = Result.success("dataset-1")

    override fun clearCachedDataSetId() = Unit

    override suspend fun uploadDataToDataSet(
        data: List<BaseData>,
    ): Result<List<BaseData>> {
        cachedData += data
        return Result.success(data)
    }

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

internal class FakeTokenProvider(
    override val isLoggedIn: Boolean = true,
) : TokenProvider {
    override suspend fun getToken(): Result<String> = Result.success("token")
    override fun clearToken() = Unit
}

/** Logged in, but every token fetch fails, as when an expired token can't be refreshed offline. */
internal class FailingTokenProvider(private val error: Throwable) : TokenProvider {
    override val isLoggedIn = true
    override suspend fun getToken(): Result<String> = Result.failure(error)
    override fun clearToken() = Unit
}

internal class FakeUserRepository : UserRepository {
    override suspend fun getCurrentUser(sessionToken: String): Result<User> =
        Result.success(User(userId = "user-1"))

    override suspend fun getUser(userId: String, sessionToken: String): Result<User> =
        throw NotImplementedError()
}
