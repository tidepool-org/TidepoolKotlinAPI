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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.tidepool.sdk.Environment
import org.tidepool.sdk.di.createTidepoolJson
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.ContinuousGlucoseData
import org.tidepool.sdk.model.data.DataDeletion
import org.tidepool.sdk.model.data.FoodData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Origins survive the outbox, so the data set's origin deduplicator can replace an updated datum,
 * and queued deletions reach the server by origin after the uploads waiting with them.
 */
class DataRepositoryImplOriginAndDeletionTest {

    private val basal = InMemoryBasalAutomatedDataDao()
    private val cgm = InMemoryContinuousGlucoseDataDao()
    private val food = InMemoryFoodDataDao()
    private val deletions = InMemoryPendingDataDeletionDao()

    /** Each request the server received, in order: its method and JSON body. */
    private val requests = mutableListOf<Pair<HttpMethod, JsonArray>>()

    /** Methods the server rejects with a 400. */
    private val rejectedMethods = mutableSetOf<HttpMethod>()

    private val origin = BasalAutomatedData.Origin(
        id = "sync-1",
        name = "org.tidepool.loop",
        type = "application",
        version = "1.0",
    )

    @Test
    fun `a food entry's origin goes out with it`() = runTest {
        val repository = repository()
        repository.uploadDataToDataSet(listOf(FoodData(id = "food-1", origin = origin)))

        repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        val uploaded = requests.single().second.single().jsonObject
        assertEquals("sync-1", uploaded.getValue("origin").jsonObject.getValue("id").jsonPrimitive.content)
        assertEquals("org.tidepool.loop", uploaded.getValue("origin").jsonObject.getValue("name").jsonPrimitive.content)
    }

    @Test
    fun `an automated basal's origin goes out with it`() = runTest {
        val repository = repository()
        repository.uploadDataToDataSet(
            listOf(
                BasalAutomatedData(
                    id = "basal-1",
                    deliveryType = BasalAutomatedData.DeliveryType.Automated,
                    duration = 1_800_000.0,
                    rate = 0.5,
                    origin = origin.copy(id = "sync-1:basal/automated"),
                )
            )
        )

        repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        val uploaded = requests.single().second.single().jsonObject
        assertEquals(
            "sync-1:basal/automated",
            uploaded.getValue("origin").jsonObject.getValue("id").jsonPrimitive.content,
        )
    }

    @Test
    fun `a deletion drops the copy still waiting and goes out after the uploads`() = runTest {
        val repository = repository()
        repository.uploadDataToDataSet(listOf(ContinuousGlucoseData(id = "cgm-1"), FoodData(id = "food-1", origin = origin)))

        repository.cacheDataDeletions(listOf(DataDeletion(id = "food-1", originId = "sync-1")))
        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(Result.success(false), result)
        assertEquals(listOf(HttpMethod.Post, HttpMethod.Delete), requests.map { it.first })
        assertEquals("cbg", requests.first().second.single().jsonObject.getValue("type").jsonPrimitive.content)
        assertEquals(
            "sync-1",
            requests.last().second.single().jsonObject
                .getValue("origin").jsonObject.getValue("id").jsonPrimitive.content,
        )
        assertTrue(food.table.rows.isEmpty())
        assertTrue(deletions.rows.isEmpty())
    }

    @Test
    fun `a deletion the server rejects stays queued`() = runTest {
        val repository = repository()
        rejectedMethods += HttpMethod.Delete
        repository.cacheDataDeletions(listOf(DataDeletion(id = "food-1", originId = "sync-1")))

        val result = repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertTrue(result.isFailure)
        assertEquals(listOf("sync-1"), deletions.rows.map { it.originId })
        assertTrue(repository.hasCachedData())
    }

    @Test
    fun `a deletion queued twice goes out once`() = runTest {
        val repository = repository()
        repository.cacheDataDeletions(listOf(DataDeletion(id = "food-1", originId = "sync-1")))
        repository.cacheDataDeletions(listOf(DataDeletion(id = "food-1", originId = "sync-1")))

        repository.uploadCachedData(sessionToken = "token", dataSetId = "data-set")

        assertEquals(1, requests.single().second.size)
        assertFalse(repository.hasCachedData())
    }

    @Test
    fun `a deletion the server couldn't select by is dropped instead of blocking the queue`() = runTest {
        val repository = repository()

        repository.cacheDataDeletions(
            listOf(
                DataDeletion(id = "food-1", originId = "x".repeat(101)),
                DataDeletion(id = "food-2", originId = "sync-2"),
            )
        )

        assertEquals(listOf("sync-2"), deletions.rows.map { it.originId })
    }

    private fun repository() = DataRepositoryImpl(
        environmentRepository = TestEnvironmentRepository,
        httpClient = HttpClient(MockEngine { request ->
            val body = Json.parseToJsonElement(request.body.toByteArray().decodeToString()).jsonArray
            requests += request.method to body
            if (request.method in rejectedMethods) {
                respond(content = "{}", status = HttpStatusCode.BadRequest, headers = jsonHeaders)
            } else {
                respond(content = "{}", status = HttpStatusCode.OK, headers = jsonHeaders)
            }
        }) {
            expectSuccess = true
            install(ContentNegotiation) { json(createTidepoolJson()) }
            defaultRequest { contentType(ContentType.Application.Json) }
        },
        basalAutomatedDataDao = basal,
        bolusDataDao = InMemoryBolusDataDao(),
        continuousGlucoseDataDao = cgm,
        dosingDecisionDataDao = InMemoryDosingDecisionDataDao(),
        foodDataDao = food,
        insulinDataDao = InMemoryInsulinDataDao(),
        deviceEventDataDao = InMemoryDeviceEventDataDao(),
        cgmSettingsDataDao = InMemoryCgmSettingsDataDao(),
        controllerSettingsDataDao = InMemoryControllerSettingsDataDao(),
        pumpSettingsDataDao = InMemoryPumpSettingsDataDao(),
        pendingDataDeletionDao = deletions,
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
