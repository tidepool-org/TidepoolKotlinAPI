package org.tidepool.sdk.service

import kotlin.time.Instant
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.ClientInfo
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.ClientSoftware
import org.tidepool.sdk.model.data.DataDeletion
import org.tidepool.sdk.model.data.DataSet
import org.tidepool.sdk.model.data.DataSource
import org.tidepool.sdk.model.data.DataType
import org.tidepool.sdk.model.data.NewDataSet
import org.tidepool.sdk.model.data.NewDataSource
import org.tidepool.sdk.repository.DataRepository
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Verifies that [DataService]'s data set lookup (exercised indirectly via
 * [DataService.uploadCachedDataNow]) picks this client's own, non-deleted data set rather than
 * the oldest one by upload id (iOS's `TidepoolService` takes the first match, not the minimum
 * upload id), and creates a new one when nothing matches. The fake repository returns its raw
 * list regardless of the requested filters, so a pass here proves the client-side filter itself
 * does the work, not just the server-side query parameters.
 */
class DataServiceDataSetSelectionTest {

    private val clientInfo = ClientInfo(name = "org.tidepool.loop", version = "TEST")

    @Test
    fun `an existing data set from this client is reused over an older one by upload id`() = runTest {
        val repository = FakeDataSetRepository(
            existingDataSets = listOf(
                DataSet(id = "other-client", client = ClientSoftware(name = "some.other.client"), uploadId = "a"),
                DataSet(id = "mine", client = ClientSoftware(name = "org.tidepool.loop"), uploadId = "z"),
            ),
        )

        dataServiceWith(repository).uploadCachedDataNow()

        assertEquals(listOf("mine"), repository.dataSetIdsUsed)
        assertEquals(0, repository.createDataSetCallCount)
        assertEquals("org.tidepool.loop", repository.lastRequestedClientName)
        assertEquals(false, repository.lastRequestedDeleted)
    }

    @Test
    fun `a deleted data set from this client is ignored`() = runTest {
        val repository = FakeDataSetRepository(
            existingDataSets = listOf(
                DataSet(
                    id = "deleted",
                    client = ClientSoftware(name = "org.tidepool.loop"),
                    deletedTime = Instant.parse("2026-01-01T00:00:00Z"),
                ),
            ),
        )

        dataServiceWith(repository).uploadCachedDataNow()

        assertEquals(listOf("created"), repository.dataSetIdsUsed)
        assertEquals(1, repository.createDataSetCallCount)
    }

    @Test
    fun `a data set from another client is ignored`() = runTest {
        val repository = FakeDataSetRepository(
            existingDataSets = listOf(DataSet(id = "other-client", client = ClientSoftware(name = "some.other.client"))),
        )

        dataServiceWith(repository).uploadCachedDataNow()

        assertEquals(listOf("created"), repository.dataSetIdsUsed)
        assertEquals(1, repository.createDataSetCallCount)
    }

    @Test
    fun `no matching data set creates a new one`() = runTest {
        val repository = FakeDataSetRepository(existingDataSets = emptyList())

        dataServiceWith(repository).uploadCachedDataNow()

        assertEquals(listOf("created"), repository.dataSetIdsUsed)
        assertEquals(1, repository.createDataSetCallCount)
    }

    private fun dataServiceWith(repository: DataRepository) = DataService(
        dataRepository = repository,
        userRepository = FakeUserRepository(),
        tokenProvider = FakeTokenProvider(),
        clientInfo = clientInfo,
    )
}

/**
 * A [DataRepository] whose [getUserDataSets] always returns [existingDataSets] as-is, ignoring
 * the requested filters, so a test can tell a client-side filter from a server-side one.
 */
private class FakeDataSetRepository(
    private val existingDataSets: List<DataSet>,
) : DataRepository {

    val dataSetIdsUsed = mutableListOf<String>()
    var createDataSetCallCount = 0
        private set
    var lastRequestedClientName: String? = null
        private set
    var lastRequestedDeleted: Boolean? = null
        private set

    override suspend fun awaitOrCreateCachedDataSetId(
        create: suspend () -> Result<String>,
    ): Result<String> = create()

    override suspend fun getUserDataSets(
        userId: String,
        sessionToken: String,
        page: Int?,
        size: Int?,
        clientName: String?,
        deleted: Boolean?,
    ): Result<List<DataSet>> {
        lastRequestedClientName = clientName
        lastRequestedDeleted = deleted
        return Result.success(existingDataSets)
    }

    override suspend fun createDataSet(
        userId: String,
        newDataSet: NewDataSet,
        sessionToken: String,
    ): Result<DataSet> {
        createDataSetCallCount++
        return Result.success(DataSet(id = "created", client = newDataSet.client))
    }

    override suspend fun uploadCachedData(sessionToken: String, dataSetId: String): Result<Boolean> {
        dataSetIdsUsed += dataSetId
        return Result.success(false)
    }

    override suspend fun hasCachedData(): Boolean = false

    override suspend fun cacheDataDeletions(deletions: List<DataDeletion>) = Unit

    override fun clearCachedDataSetId() = Unit

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
