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

/**
 * Opens a real version 1 outbox (the schema Room generated before version 2) with the current
 * code, so [MIGRATION_1_2] has to bring it to a schema Room accepts without losing queued rows.
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
    fun versionOneOutboxKeepsItsQueuedRows() = runTest {
        createVersionOneDatabase(VERSION_1_SCHEMA)

        val migrated = openCurrentDatabase()

        val food = migrated.foodDataDao().getAll().single()
        assertEquals("food-1", food.id)
        assertNull(food.origin)
        assertEquals("food-1", migrated.foodDataDao().getById("food-1")?.id)
        val basal = migrated.basalAutomatedDataDao().getAll().single()
        assertEquals("basal-1", basal.id)
        assertNull(basal.origin)
        assertNull(basal.payload)
        assertNull(basal.suppressed)
        assertEquals(1.5, migrated.bolusDataDao().getAll().single().normal)
    }

    @Test
    fun migratedOutboxStoresOriginsAndQueuedDeletions() = runTest {
        createVersionOneDatabase(VERSION_1_SCHEMA)
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

    // A version 1 outbox from before `bolus_data.normal` existed gets the column instead of failing.
    @Test
    fun versionOneOutboxWithoutBolusNormalGetsTheColumn() = runTest {
        createVersionOneDatabase(
            VERSION_1_SCHEMA.map { it.replace(", `normal` REAL", "") },
            bolusNormal = false,
        )

        val migrated = openCurrentDatabase()

        assertNull(migrated.bolusDataDao().getAll().single().normal)
    }

    // The oldest version 1 outboxes predate the settings and device event tables too.
    @Test
    fun oldestVersionOneOutboxGetsTheMissingTables() = runTest {
        val laterTables = listOf("device_event_data", "cgm_settings_data", "controller_settings_data", "pump_settings_data")
        createVersionOneDatabase(
            VERSION_1_SCHEMA
                .filterNot { statement -> laterTables.any { statement.contains("`$it`") } }
                .map { it.replace(", `normal` REAL", "") },
            bolusNormal = false,
        )

        val migrated = openCurrentDatabase()

        assertEquals("food-1", migrated.foodDataDao().getAll().single().id)
        assertEquals(emptyList(), migrated.pumpSettingsDataDao().getAll())
    }

    private fun createVersionOneDatabase(schema: List<String>, bolusNormal: Boolean = true) {
        BundledSQLiteDriver().open(databaseFile.absolutePath).apply {
            schema.forEach { execSQL(it) }
            execSQL(
                "INSERT INTO `food_data` (`id`, `type`, `time`, `associations`, `notes`, `name`, `nutrition`) " +
                    "VALUES ('food-1', '\"food\"', 100, '[]', '[]', 'Taco', NULL)"
            )
            execSQL(
                "INSERT INTO `basal_automated_data` (`id`, `type`, `time`, `delivery_type`, `duration`, `rate`) " +
                    "VALUES ('basal-1', '\"basal\"', 100, '\"automated\"', 30.0, 0.5)"
            )
            if (bolusNormal) {
                execSQL(
                    "INSERT INTO `bolus_data` (`id`, `type`, `time`, `sub_type`, `delivery_context`, `normal`) " +
                        "VALUES ('bolus-1', '\"bolus\"', 100, 'normal', 'device', 1.5)"
                )
            } else {
                execSQL(
                    "INSERT INTO `bolus_data` (`id`, `type`, `time`, `sub_type`, `delivery_context`) " +
                        "VALUES ('bolus-1', '\"bolus\"', 100, 'normal', 'device')"
                )
            }
            execSQL("PRAGMA user_version = 1")
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
        /** Version 1's schema, copied from Room's generated `LoopKitDatabase_Impl` before the bump. */
        val VERSION_1_SCHEMA = listOf(
        """CREATE TABLE IF NOT EXISTS `BaseDataEntity` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clockDriftOffset` INTEGER, `conversionOffset` INTEGER, `dataSetId` TEXT, `deviceTime` TEXT, `notes` TEXT, `timeZone` TEXT, `timeZoneOffset` INTEGER, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `basal_automated_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `delivery_type` TEXT NOT NULL, `duration` REAL NOT NULL, `expected_duration` REAL, `rate` REAL NOT NULL, `schedule_name` TEXT, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `bolus_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `sub_type` TEXT NOT NULL, `delivery_context` TEXT NOT NULL, `normal` REAL, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `continuous_glucose_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `value` REAL, `units` TEXT, `trend` TEXT, `trend_rate` REAL, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `dosing_decision_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `reason` TEXT NOT NULL, `carbs_on_board` TEXT, `insulin_on_board` TEXT, `recommended_basal` TEXT, `recommended_bolus` TEXT, `requested_bolus` TEXT, `schedule_time_zone_offset` INTEGER, `units` TEXT NOT NULL, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `food_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `brand` TEXT, `code` TEXT, `meal` TEXT, `meal_other` TEXT, `name` TEXT, `amount` TEXT, `nutrition` TEXT, `ingredients` TEXT, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `insulin_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `dose` TEXT NOT NULL, `site` TEXT, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `device_event_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `sub_type` TEXT NOT NULL, `device_identifier` TEXT, `expected_lifetime_seconds` REAL, `warmup_period_seconds` REAL, `failure_message` TEXT, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `cgm_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `controller_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS `pump_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, PRIMARY KEY(`id`))""",
        """CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)""",
        """INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fc9eabadc1bd06011667dede19835dd7')""",
        )
    }
}
