package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
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
import org.tidepool.sdk.model.data.BaseData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import org.tidepool.sdk.model.data.DosingDecisionData
import org.tidepool.sdk.model.data.FoodData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Exercises the real outbox drain in [DataRepositoryImpl.uploadCachedData]: per-type requests,
 * per-type limits, deleting only what the server accepted, and one failing type not holding back
 * the others. The DAOs are in memory, so Room's SQL itself isn't covered.
 */
class DataRepositoryImplUploadTest {

    private val basal = InMemoryBasalAutomatedDataDao()
    private val bolus = InMemoryBolusDataDao()
    private val cgm = InMemoryContinuousGlucoseDataDao()
    private val dosingDecisions = InMemoryDosingDecisionDataDao()
    private val food = InMemoryFoodDataDao()
    private val insulin = InMemoryInsulinDataDao()

    /** The data types of each request the server received, in order. */
    private val requests = mutableListOf<Set<String>>()

    /** Data types the server rejects with a 400. */
    private val rejectedTypes = mutableSetOf<String>()

    /** Body a rejected request gets back. */
    private var rejectedResponseBody = "{}"

    @Test
    fun `each data type goes out in its own request`() = runTest {
        val repository = repository()
        repository.cache(ContinuousGlucoseData(id = "cgm-1"), FoodData(id = "food-1"), dosingDecision("dd-1"))

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertEquals(listOf(setOf("cbg"), setOf("dosingDecision"), setOf("food")), requests)
        assertTrue(cgm.table.rows.isEmpty() && food.table.rows.isEmpty() && dosingDecisions.table.rows.isEmpty())
    }

    @Test
    fun `a type at its limit reports a full batch and leaves the rest cached`() = runTest {
        val repository = repository()
        repository.cache(*Array(1001) { ContinuousGlucoseData(id = "cgm-$it") })

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(true), result)
        assertEquals(listOf("cgm-1000"), cgm.table.rows.map { it.id })
    }

    @Test
    fun `dosing decisions are capped at 50 per request`() = runTest {
        val repository = repository()
        repository.cache(*Array(51) { dosingDecision("dd-$it") })

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(true), result)
        assertEquals(listOf("dd-50"), dosingDecisions.table.rows.map { it.id })
    }

    @Test
    fun `a rejected type stays cached without holding back the others`() = runTest {
        rejectedTypes += "cbg"
        val repository = repository()
        repository.cache(ContinuousGlucoseData(id = "cgm-1"), FoodData(id = "food-1"))

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertEquals(listOf("cgm-1"), cgm.table.rows.map { it.id })
        assertTrue(food.table.rows.isEmpty())
    }

    @Test
    fun `nothing getting through is a failure and keeps everything cached`() = runTest {
        rejectedTypes += setOf("cbg", "food")
        val repository = repository()
        repository.cache(ContinuousGlucoseData(id = "cgm-1"), FoodData(id = "food-1"))

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertTrue(result.isFailure)
        assertEquals(1, cgm.table.rows.size)
        assertEquals(1, food.table.rows.size)
    }

    @Test
    fun `hasCachedData reports whether anything is waiting`() = runTest {
        val repository = repository()
        assertFalse(repository.hasCachedData())

        repository.cache(FoodData(id = "food-1"))
        assertTrue(repository.hasCachedData())

        repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")
        assertFalse(repository.hasCachedData())
    }

    // Before this fix, a 400/422 left the whole batch cached regardless of its body, so a single
    // permanently invalid row (e.g. an out-of-range value) sat at the front of `time ASC, rowid
    // ASC` order and blocked every row behind it, forever: confirmed by temporarily asserting
    // that `cgm.table.rows` still held both an old rejected row and a newer one after two
    // `uploadCachedData` passes, with no change to production code. These tests cover the fix.
    @Test
    fun `a rejected row named by pointer is dropped without blocking the rest of the batch`() = runTest {
        rejectedResponseBody = """
            {"code":"value-out-of-range","errors":[{"code":"value-out-of-range","title":"value is out of range","detail":"value 1000 is not between 0 and 1000","source":{"pointer":"/0/value"},"meta":{"type":"cbg"}}]}
        """.trimIndent()
        rejectedTypes += "cbg"
        val repository = repository()
        repository.cache(ContinuousGlucoseData(id = "cgm-bad"), ContinuousGlucoseData(id = "cgm-ok"))

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertEquals(listOf("cgm-ok"), cgm.table.rows.map { it.id })
    }

    @Test
    fun `multiple rows named by pointer in one rejection are all dropped`() = runTest {
        rejectedResponseBody = """
            {"errors":[{"source":{"pointer":"/0/value"},"meta":{"type":"cbg"}},{"source":{"pointer":"/2/value"},"meta":{"type":"cbg"}}]}
        """.trimIndent()
        rejectedTypes += "cbg"
        val repository = repository()
        repository.cache(
            ContinuousGlucoseData(id = "cgm-bad-0"),
            ContinuousGlucoseData(id = "cgm-ok-1"),
            ContinuousGlucoseData(id = "cgm-bad-2"),
        )

        repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(listOf("cgm-ok-1"), cgm.table.rows.map { it.id })
    }

    @Test
    fun `the surviving rows of a partially rejected batch upload on the next pass`() = runTest {
        rejectedResponseBody = """{"errors":[{"source":{"pointer":"/0/value"},"meta":{"type":"cbg"}}]}"""
        rejectedTypes += "cbg"
        val repository = repository()
        repository.cache(ContinuousGlucoseData(id = "cgm-bad"), ContinuousGlucoseData(id = "cgm-ok"))
        repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")
        rejectedTypes.clear()

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertFalse(repository.hasCachedData())
    }

    @Test
    fun `a rejection body that can't be parsed into pointers leaves the whole batch cached`() = runTest {
        // No "errors" key at all: today's behaviour (the whole batch stays cached) applies.
        rejectedResponseBody = """{"code":"internal-server-error"}"""
        rejectedTypes += "cbg"
        val repository = repository()
        repository.cache(ContinuousGlucoseData(id = "cgm-1"))

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertTrue(result.isFailure)
        assertEquals(listOf("cgm-1"), cgm.table.rows.map { it.id })
    }

    @Test
    fun `an empty outbox sends nothing`() = runTest {
        val result = repository().uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertTrue(requests.isEmpty())
    }

    private suspend fun DataRepositoryImpl.cache(vararg data: BaseData) {
        assertFalse(uploadDataToDataSet(data.toList()).isFailure)
    }

    private fun dosingDecision(id: String) = DosingDecisionData(id = id, reason = "loop")

    private fun repository() = DataRepositoryImpl(
        environmentRepository = TestEnvironmentRepository,
        httpClient = HttpClient(MockEngine { request ->
            val types = Json.parseToJsonElement(request.body.toByteArray().decodeToString())
                .jsonArray
                .map { it.jsonObject.getValue("type").jsonPrimitive.content }
                .toSet()
            requests += types
            if (types.any { it in rejectedTypes }) {
                respond(content = rejectedResponseBody, status = HttpStatusCode.BadRequest, headers = jsonHeaders)
            } else {
                respond(content = "{}", status = HttpStatusCode.OK, headers = jsonHeaders)
            }
        }) {
            // The parts of the production client (DataModule) this path relies on.
            expectSuccess = true
            install(ContentNegotiation) { json(createTidepoolJson()) }
            defaultRequest { contentType(ContentType.Application.Json) }
        },
        basalAutomatedDataDao = basal,
        bolusDataDao = bolus,
        continuousGlucoseDataDao = cgm,
        dosingDecisionDataDao = dosingDecisions,
        foodDataDao = food,
        insulinDataDao = insulin,
        deviceEventDataDao = InMemoryDeviceEventDataDao(),
        cgmSettingsDataDao = InMemoryCgmSettingsDataDao(),
        controllerSettingsDataDao = InMemoryControllerSettingsDataDao(),
        pumpSettingsDataDao = InMemoryPumpSettingsDataDao(),
        smbgDataDao = InMemorySmbgDataDao(),
        pumpStatusDataDao = InMemoryPumpStatusDataDao(),
        controllerStatusDataDao = InMemoryControllerStatusDataDao(),
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
