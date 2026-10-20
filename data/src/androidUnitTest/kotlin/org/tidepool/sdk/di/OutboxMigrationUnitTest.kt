package org.tidepool.sdk.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.database.LoopKitDatabase
import org.tidepool.sdk.database.entity.PendingDataDeletionEntity
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Opens real version 1 outboxes with the current code, so [MIGRATION_1_2] has to bring each to a
 * schema Room accepts. The fixtures in `resources/outbox-v1` are the schemas Room generated at the
 * SDK commits the app shipped, one per version 1 shape, named by date and commit.
 */
class OutboxMigrationUnitTest {

    private val directory = createTempDirectory("outbox-migration").toFile()
    private val databaseFile = File(directory, "loop-kit-database")
    private var database: LoopKitDatabase? = null

    @AfterTest
    fun cleanUp() {
        database?.close()
        directory.deleteRecursively()
    }

    @Test
    fun lastVersionOneOutboxKeepsItsQueuedRows() = runTest {
        createVersionOneDatabase(schema(LAST_VERSION_1))
        insertRows(durationMinutes = true, bolusNormal = true)

        val migrated = openCurrentDatabase()

        val food = migrated.foodDataDao().getAll().single()
        assertEquals("food-1", food.id)
        assertNull(food.origin)
        assertEquals("food-1", migrated.foodDataDao().getById("food-1")?.id)
        val basal = migrated.basalAutomatedDataDao().getAll().single()
        assertEquals("basal-1", basal.id)
        assertEquals(30.0, basal.duration)
        assertNull(basal.origin)
        assertNull(basal.payload)
        assertNull(basal.suppressed)
        assertEquals(1.5, migrated.bolusDataDao().getAll().single().normal)
    }

    @Test
    fun migratedOutboxStoresOriginsAndQueuedDeletions() = runTest {
        createVersionOneDatabase(schema(LAST_VERSION_1))
        insertRows(durationMinutes = true, bolusNormal = true)
        val migrated = openCurrentDatabase()

        val food = migrated.foodDataDao().getAll().single()
        migrated.foodDataDao().insert(food.copy(origin = """{"id":"sync-1"}"""))
        migrated.pendingDataDeletionDao().insert(listOf(PendingDataDeletionEntity(originId = "sync-1")))

        assertEquals("""{"id":"sync-1"}""", migrated.foodDataDao().getById("food-1")?.origin)
        val basal = migrated.basalAutomatedDataDao().getAll().single()
        migrated.basalAutomatedDataDao().insert(basal.copy(payload = """{"deliveredUnits":0.25}"""))
        assertEquals("""{"deliveredUnits":0.25}""", migrated.basalAutomatedDataDao().getById("basal-1")?.payload)
        assertEquals(listOf("sync-1"), migrated.pendingDataDeletionDao().getAll(10).map { it.originId })
    }

    // Room hasn't opened these since a later schema shipped, so their rows were already out of
    // reach; rebuilding them empty is what lets the outbox open again.
    @Test
    fun everyEarlierVersionOneOutboxIsRebuiltEmpty() = runTest {
        for (fixture in EARLIER_VERSION_1) {
            createVersionOneDatabase(schema(fixture))
            insertRows(
                durationMinutes = fixture == BEFORE_FOOD_DETAILS,
                bolusNormal = fixture == BEFORE_FOOD_DETAILS,
            )

            val migrated = openCurrentDatabase()

            assertEquals(emptyList(), migrated.foodDataDao().getAll(), fixture)
            assertEquals(emptyList(), migrated.basalAutomatedDataDao().getAll(), fixture)
            assertEquals(emptyList(), migrated.bolusDataDao().getAll(), fixture)
            assertEquals(emptyList(), migrated.pumpSettingsDataDao().getAll(), fixture)
            migrated.pendingDataDeletionDao().insert(listOf(PendingDataDeletionEntity(originId = "sync-1")))
            assertEquals(1, migrated.pendingDataDeletionDao().getAll(10).size, fixture)

            migrated.close()
            database = null
            directory.listFiles()?.forEach { it.delete() }
        }
    }

    @Test
    fun outboxWithoutAStoredIdentityIsRebuiltEmpty() = runTest {
        createVersionOneDatabase(schema(LAST_VERSION_1).filterNot { it.contains("room_master_table") })
        insertRows(durationMinutes = true, bolusNormal = true)

        val migrated = openCurrentDatabase()

        assertTrue(migrated.foodDataDao().getAll().isEmpty())
    }

    private fun schema(fixture: String): List<String> =
        checkNotNull(javaClass.getResource("/outbox-v1/$fixture.sql")) { "Missing fixture $fixture" }
            .readText()
            .lines()
            .filter { it.isNotBlank() }

    private fun createVersionOneDatabase(schema: List<String>) {
        BundledSQLiteDriver().open(databaseFile.absolutePath).apply {
            schema.forEach { execSQL(it) }
            execSQL("PRAGMA user_version = 1")
            close()
        }
    }

    // Only columns every version 1 shape has, apart from the ones the flags name.
    private fun insertRows(durationMinutes: Boolean, bolusNormal: Boolean) {
        BundledSQLiteDriver().open(databaseFile.absolutePath).apply {
            execSQL(
                "INSERT INTO `food_data` (`id`, `type`, `time`, `associations`, `notes`, `name`) " +
                    "VALUES ('food-1', '\"food\"', 100, '[]', '[]', 'Taco')"
            )
            execSQL(
                "INSERT INTO `basal_automated_data` (`id`, `type`, `time`, `delivery_type`, `duration`, `rate`) " +
                    "VALUES ('basal-1', '\"basal\"', 100, '\"automated\"', " +
                    "${if (durationMinutes) "30.0" else "1800000"}, 0.5)"
            )
            execSQL(
                "INSERT INTO `bolus_data` (`id`, `type`, `time`, `sub_type`, `delivery_context`" +
                    "${if (bolusNormal) ", `normal`" else ""}) " +
                    "VALUES ('bolus-1', '\"bolus\"', 100, 'normal', 'device'${if (bolusNormal) ", 1.5" else ""})"
            )
            close()
        }
    }

    private fun openCurrentDatabase(): LoopKitDatabase {
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        every { context.getDatabasePath(any()) } returns databaseFile
        return Room.databaseBuilder<LoopKitDatabase>(context, databaseFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .addMigrations(MIGRATION_1_2)
            .build()
            .also { database = it }
    }

    private companion object {
        /** Before the settings and device event tables. */
        const val BEFORE_SETTINGS_TABLES = "2025-11-07-0825c6a"

        /** Before `bolus_data.normal` and basal durations in minutes. */
        const val BEFORE_MINUTES = "2025-11-26-f9d014d"

        /** Before food amounts, nutrition and ingredients. */
        const val BEFORE_FOOD_DETAILS = "2026-02-26-729882e"

        /** The last version 1 schema. */
        const val LAST_VERSION_1 = "2026-10-06-ec75f6d"

        val EARLIER_VERSION_1 = listOf(BEFORE_SETTINGS_TABLES, BEFORE_MINUTES, BEFORE_FOOD_DETAILS)
    }
}
