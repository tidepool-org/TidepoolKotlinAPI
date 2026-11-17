package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.Environment
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.model.data.CgmSettingsData
import org.tidepool.sdk.model.data.ControllerSettingsData
import org.tidepool.sdk.model.data.ControllerStatusData
import org.tidepool.sdk.model.data.DataDeletion
import org.tidepool.sdk.model.data.Origin
import org.tidepool.sdk.model.data.PumpSettingsData
import org.tidepool.sdk.model.data.PumpStatusData
import org.tidepool.sdk.model.data.SmbgData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * smbg, pumpStatus and controllerStatus (items #3/#2) go through the outbox drain the same way
 * as the other origin-carrying types: their own request, and a waiting copy whose origin has a
 * queued deletion is dropped instead of sent. pumpSettings/cgmSettings/controllerSettings (item
 * #1) are also exercised here: they used to be cached but never drained.
 */
class NewDatumTypesUploadTest {

    private val smbg = InMemorySmbgDataDao()
    private val pumpStatus = InMemoryPumpStatusDataDao()
    private val controllerStatus = InMemoryControllerStatusDataDao()
    private val cgmSettings = InMemoryCgmSettingsDataDao()
    private val controllerSettings = InMemoryControllerSettingsDataDao()
    private val pumpSettings = InMemoryPumpSettingsDataDao()

    private val requests = mutableListOf<Set<String>>()

    private val origin = Origin(id = "sync-1", name = "org.tidepool.loop", type = "application", version = "1.0")

    @Test
    fun `smbg, pumpStatus and controllerStatus each go out in their own request`() = runTest {
        val repository = repository()
        repository.uploadDataToDataSet(
            listOf(
                SmbgData(id = "smbg-1", origin = origin.copy(id = "sync-1:smbg")),
                PumpStatusData(id = "pump-status-1", origin = origin.copy(id = "sync-2:pumpStatus")),
                ControllerStatusData(id = "controller-status-1", origin = origin.copy(id = "sync-3:controllerStatus")),
            )
        )

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertEquals(setOf(setOf("smbg"), setOf("pumpStatus"), setOf("controllerStatus")), requests.toSet())
        assertTrue(smbg.table.getAll().isEmpty())
        assertTrue(pumpStatus.table.getAll().isEmpty())
        assertTrue(controllerStatus.table.getAll().isEmpty())
    }

    @Test
    fun `a waiting smbg, pumpStatus or controllerStatus whose origin has a queued deletion is dropped instead of sent`() =
        runTest {
            val repository = repository()
            repository.uploadDataToDataSet(
                listOf(
                    SmbgData(id = "smbg-1", origin = origin.copy(id = "sync-1:smbg")),
                    PumpStatusData(id = "pump-status-1", origin = origin.copy(id = "sync-2:pumpStatus")),
                    ControllerStatusData(id = "controller-status-1", origin = origin.copy(id = "sync-3:controllerStatus")),
                )
            )
            repository.cacheDataDeletions(
                listOf(
                    DataDeletion(id = "smbg-1", originId = "sync-1:smbg"),
                    DataDeletion(id = "pump-status-1", originId = "sync-2:pumpStatus"),
                    DataDeletion(id = "controller-status-1", originId = "sync-3:controllerStatus"),
                )
            )

            repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

            assertTrue(requests.none { "smbg" in it || "pumpStatus" in it || "controllerStatus" in it })
            assertFalse(repository.hasCachedData())
        }

    @Test
    fun `pumpSettings, cgmSettings and controllerSettings are drained and counted by hasCachedData`() = runTest {
        val repository = repository()
        assertFalse(repository.hasCachedData())

        repository.uploadDataToDataSet(
            listOf(
                PumpSettingsData(id = "pump-settings-1"),
                CgmSettingsData(id = "cgm-settings-1"),
                ControllerSettingsData(id = "controller-settings-1"),
            )
        )
        assertTrue(repository.hasCachedData())

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertEquals(
            setOf(setOf("pumpSettings"), setOf("cgmSettings"), setOf("controllerSettings")),
            requests.toSet(),
        )
        assertFalse(repository.hasCachedData())
    }

    private fun repository() = DataRepositoryImpl(
        environmentRepository = TestEnvironmentRepository,
        httpClient = HttpClient(MockEngine { request ->
            if (request.method == HttpMethod.Post) {
                val types = Json.parseToJsonElement(request.body.toByteArray().decodeToString())
                    .jsonArray
                    .map { it.jsonObject.getValue("type").jsonPrimitive.content }
                    .toSet()
                if (types.isNotEmpty()) requests += types
            }
            respond(content = "{}", status = HttpStatusCode.OK, headers = jsonHeaders)
        }) {
            expectSuccess = true
            install(ContentNegotiation) { json(createTidepoolJson()) }
            defaultRequest { contentType(ContentType.Application.Json) }
        },
        basalAutomatedDataDao = InMemoryBasalAutomatedDataDao(),
        bolusDataDao = InMemoryBolusDataDao(),
        continuousGlucoseDataDao = InMemoryContinuousGlucoseDataDao(),
        dosingDecisionDataDao = InMemoryDosingDecisionDataDao(),
        foodDataDao = InMemoryFoodDataDao(),
        insulinDataDao = InMemoryInsulinDataDao(),
        deviceEventDataDao = InMemoryDeviceEventDataDao(),
        cgmSettingsDataDao = cgmSettings,
        controllerSettingsDataDao = controllerSettings,
        pumpSettingsDataDao = pumpSettings,
        smbgDataDao = smbg,
        pumpStatusDataDao = pumpStatus,
        controllerStatusDataDao = controllerStatus,
        pendingDataDeletionDao = InMemoryPendingDataDeletionDao(),
        keyValueStorage = InMemoryKeyValueStorage(),
    )

    private object TestEnvironmentRepository : EnvironmentRepository {
        override suspend fun getEnvironmentOptions() = Result.success(listOf(Environment.Production))
        override fun setEnvironment(environment: Environment) = Unit
        override fun getEnvironment() = Environment("test.invalid")
    }

    private companion object {
        val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    }
}
