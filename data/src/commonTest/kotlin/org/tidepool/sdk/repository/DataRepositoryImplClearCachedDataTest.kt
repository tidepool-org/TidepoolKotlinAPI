package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.Environment
import org.tidepool.sdk.database.entity.PendingDataDeletionEntity
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import org.tidepool.sdk.model.data.DataDeletion
import org.tidepool.sdk.model.data.FoodData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * There was no way to empty the local outbox before `clearCachedData`: the only thing that ever
 * removed cached rows was a successful upload, so a host that wanted to drop local state (e.g. on
 * logout) had no way to do it.
 */
class DataRepositoryImplClearCachedDataTest {

    private val basal = InMemoryBasalAutomatedDataDao()
    private val bolus = InMemoryBolusDataDao()
    private val cgm = InMemoryContinuousGlucoseDataDao()
    private val dosingDecisions = InMemoryDosingDecisionDataDao()
    private val food = InMemoryFoodDataDao()
    private val insulin = InMemoryInsulinDataDao()
    private val deviceEvent = InMemoryDeviceEventDataDao()
    private val cgmSettings = InMemoryCgmSettingsDataDao()
    private val controllerSettings = InMemoryControllerSettingsDataDao()
    private val pumpSettings = InMemoryPumpSettingsDataDao()
    private val smbg = InMemorySmbgDataDao()
    private val pumpStatus = InMemoryPumpStatusDataDao()
    private val controllerStatus = InMemoryControllerStatusDataDao()
    private val deletions = InMemoryPendingDataDeletionDao()
    private val keyValueStorage = InMemoryKeyValueStorage()

    @Test
    fun `clearCachedData empties every outbox table and the cached data set id`() = runTest {
        val repository = repository()
        repository.uploadDataToDataSet(listOf(ContinuousGlucoseData(id = "cgm-1"), FoodData(id = "food-1")))
        repository.cacheDataDeletions(listOf(DataDeletion(id = "food-2", originId = "sync-1")))
        deviceEvent.table.insert(
            org.tidepool.sdk.database.entity.data.DeviceEventDataEntity(
                id = "device-1",
                type = "deviceEvent",
                timeZoneOffset = null,
                subType = "sensorStart",
            )
        )
        var createCalls = 0
        val firstId = repository.awaitOrCreateCachedDataSetId {
            createCalls++
            Result.success("existing-data-set")
        }
        assertEquals(Result.success("existing-data-set"), firstId)
        assertTrue(repository.hasCachedData())

        val result = repository.clearCachedData()

        assertTrue(result.isSuccess)
        assertFalse(repository.hasCachedData())
        assertTrue(basal.table.getAll().isEmpty())
        assertTrue(bolus.table.getAll().isEmpty())
        assertTrue(cgm.table.getAll().isEmpty())
        assertTrue(dosingDecisions.table.getAll().isEmpty())
        assertTrue(food.table.getAll().isEmpty())
        assertTrue(insulin.table.getAll().isEmpty())
        assertTrue(deviceEvent.table.getAll().isEmpty())
        assertTrue(smbg.table.getAll().isEmpty())
        assertTrue(pumpStatus.table.getAll().isEmpty())
        assertTrue(controllerStatus.table.getAll().isEmpty())
        assertTrue(deletions.rows.isEmpty())

        // The cached data set id was cleared too: asking again invokes `create` a second time.
        val secondId = repository.awaitOrCreateCachedDataSetId {
            createCalls++
            Result.success("new-data-set")
        }
        assertEquals(Result.success("new-data-set"), secondId)
        assertEquals(2, createCalls)
    }

    @Test
    fun `clearCachedData works without a session`() = runTest {
        // No token, no HTTP call: the mock engine below would fail the test if it were ever hit.
        val result = repository().clearCachedData()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `a data set id cached by the old lowest-uploadId selection is picked again`() = runTest {
        keyValueStorage.putString("KEY_CACHED_DATA_SET_ID", "another-clients-data-set")

        val id = repository().awaitOrCreateCachedDataSetId { Result.success("this-clients-data-set") }

        assertEquals(Result.success("this-clients-data-set"), id)
    }

    private fun repository() = DataRepositoryImpl(
        environmentRepository = TestEnvironmentRepository,
        httpClient = HttpClient(MockEngine { respond(content = "{}", status = HttpStatusCode.OK) }),
        basalAutomatedDataDao = basal,
        bolusDataDao = bolus,
        continuousGlucoseDataDao = cgm,
        dosingDecisionDataDao = dosingDecisions,
        foodDataDao = food,
        insulinDataDao = insulin,
        deviceEventDataDao = deviceEvent,
        cgmSettingsDataDao = cgmSettings,
        controllerSettingsDataDao = controllerSettings,
        pumpSettingsDataDao = pumpSettings,
        smbgDataDao = smbg,
        pumpStatusDataDao = pumpStatus,
        controllerStatusDataDao = controllerStatus,
        pendingDataDeletionDao = deletions,
        keyValueStorage = keyValueStorage,
    )

    private object TestEnvironmentRepository : EnvironmentRepository {
        override suspend fun getEnvironmentOptions() = Result.success(listOf(Environment.Production))
        override fun setEnvironment(environment: Environment) = Unit
        override fun getEnvironment() = Environment("test.invalid")
    }
}
