package org.tidepool.sdk.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.execSQL
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.module.Module
import org.koin.dsl.module
import org.tidepool.sdk.database.LoopKitDatabase
import org.tidepool.sdk.api.createAlertApi
import org.tidepool.sdk.api.createAuthorizationApi
import org.tidepool.sdk.api.createBlobApi
import org.tidepool.sdk.api.createClinicApi
import org.tidepool.sdk.api.createConfirmationApi
import org.tidepool.sdk.api.createDataApi
import org.tidepool.sdk.api.createGeneralApi
import org.tidepool.sdk.api.createMessageApi
import org.tidepool.sdk.api.createMetadataApi
import org.tidepool.sdk.api.createMetricsApi
import org.tidepool.sdk.api.createPrescriptionApi
import org.tidepool.sdk.api.createSummaryApi
import org.tidepool.sdk.api.createTaskApi
import org.tidepool.sdk.api.createUserApi
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import org.tidepool.sdk.AndroidKeyValueStorage
import org.tidepool.sdk.DnsResolver
import org.tidepool.sdk.DnsResolverImpl
import org.tidepool.sdk.fake.FakeBackendInterceptor
import org.tidepool.sdk.fake.FakeBackendRecorder
import org.tidepool.sdk.repository.KeyValueStorage
import kotlin.math.roundToLong

actual val platformDataModule: Module
    get() = module {
        single {
            Room.databaseBuilder<LoopKitDatabase>(
                context = get(),
                name = "loop-kit-database",
            )
                .setDriver(BundledSQLiteDriver())
                .addMigrations(*OUTBOX_MIGRATIONS)
                .build()
        }
        single<Logger> {
            Logger.ANDROID
        }
        single<KeyValueStorage> {
            AndroidKeyValueStorage(context = get())
        }
        single<DnsResolver> {
            DnsResolverImpl()
        }
        single<HttpClientEngine> {
            // Only a caller that passes a recorder gets the fake backend; everyone else gets a
            // plain OkHttp engine.
            val fakeBackendRecorder = getOrNull<FakeBackendRecorder>()
            OkHttp.create {
                fakeBackendRecorder?.let { addInterceptor(FakeBackendInterceptor(it)) }
            }
        }
    }

/**
 * Version 2: an origin on food and automated basal rows, the basal payload and suppressed basal the
 * outbox used to drop, and the queued-deletion table.
 *
 * Version 1's schema changed three times without a bump: the settings and device event tables
 * (2025-11), `bolus_data.normal` and basal durations in minutes instead of milliseconds (2026-02),
 * and food amounts, nutrition and ingredients (2026-03). Room refuses to open a version 1 outbox
 * whose schema isn't the one the code was built with, so an outbox made before the last change
 * hasn't opened since, and its queued rows were already out of reach. Only an outbox with the last
 * version 1 schema, recognised by the identity hash Room stored with it, is migrated in place; any
 * other is rebuilt empty, so the outbox opens again.
 */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        migrationToVersion2 { sql ->
            db.query(sql).use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
        }.forEach { db.execSQL(it) }
    }

    override fun migrate(connection: SQLiteConnection) {
        migrationToVersion2 { sql ->
            connection.prepare(sql).use { statement ->
                if (statement.step() && !statement.isNull(0)) statement.getText(0) else null
            }
        }.forEach { connection.execSQL(it) }
    }
}

/** The identity hash Room stored with the last version 1 schema, in use from 2026-03 until version 2. */
internal const val LAST_VERSION_1_IDENTITY_HASH = "fc9eabadc1bd06011667dede19835dd7"

private fun migrationToVersion2(firstText: (sql: String) -> String?): List<String> {
    val hasMasterTable = firstText(
        "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'room_master_table'"
    ) != null
    val identityHash = if (hasMasterTable) {
        firstText("SELECT identity_hash FROM room_master_table WHERE id = 42")
    } else {
        null
    }
    return if (identityHash == LAST_VERSION_1_IDENTITY_HASH) {
        LAST_VERSION_1_TO_VERSION_2
    } else {
        VERSION_2_TABLE_NAMES.map { "DROP TABLE IF EXISTS `$it`" } + VERSION_2_TABLES
    }
}

private val LAST_VERSION_1_TO_VERSION_2 = listOf(
    "ALTER TABLE `food_data` ADD COLUMN `origin` TEXT",
    "ALTER TABLE `basal_automated_data` ADD COLUMN `origin` TEXT",
    "ALTER TABLE `basal_automated_data` ADD COLUMN `payload` TEXT",
    "ALTER TABLE `basal_automated_data` ADD COLUMN `suppressed` TEXT",
    "CREATE TABLE IF NOT EXISTS `pending_data_deletion` (`origin_id` TEXT NOT NULL, PRIMARY KEY(`origin_id`))",
)

/**
 * Version 2's tables as `schemas/.../2.json` records them. Frozen: later versions migrate from
 * these. Internal for the test that migrates a version 2 outbox.
 */
internal val VERSION_2_TABLES = listOf(
    "CREATE TABLE IF NOT EXISTS `BaseDataEntity` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clockDriftOffset` INTEGER, `conversionOffset` INTEGER, `dataSetId` TEXT, `deviceTime` TEXT, `notes` TEXT, `timeZone` TEXT, `timeZoneOffset` INTEGER, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `basal_automated_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `delivery_type` TEXT NOT NULL, `duration` REAL NOT NULL, `expected_duration` REAL, `rate` REAL NOT NULL, `schedule_name` TEXT, `origin` TEXT, `payload` TEXT, `suppressed` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `bolus_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `sub_type` TEXT NOT NULL, `delivery_context` TEXT NOT NULL, `normal` REAL, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `continuous_glucose_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `value` REAL, `units` TEXT, `trend` TEXT, `trend_rate` REAL, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `dosing_decision_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `reason` TEXT NOT NULL, `carbs_on_board` TEXT, `insulin_on_board` TEXT, `recommended_basal` TEXT, `recommended_bolus` TEXT, `requested_bolus` TEXT, `schedule_time_zone_offset` INTEGER, `units` TEXT NOT NULL, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `food_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `brand` TEXT, `code` TEXT, `meal` TEXT, `meal_other` TEXT, `name` TEXT, `amount` TEXT, `nutrition` TEXT, `ingredients` TEXT, `origin` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `insulin_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `dose` TEXT NOT NULL, `site` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `device_event_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `sub_type` TEXT NOT NULL, `device_identifier` TEXT, `expected_lifetime_seconds` REAL, `warmup_period_seconds` REAL, `failure_message` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `cgm_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `controller_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `pump_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `pending_data_deletion` (`origin_id` TEXT NOT NULL, PRIMARY KEY(`origin_id`))",
)

private val VERSION_2_TABLE_NAMES = VERSION_2_TABLES.map { it.substringAfter('`').substringBefore('`') }

/**
 * Version 3: an origin on bolus and insulin rows too, so a running dose's upload is replaced by
 * the finished one and a queued deletion can drop a copy still waiting, as for basal and food.
 * Every version 2 outbox, migrated in place or rebuilt from version 1, has the frozen version 2
 * tables, so adding the two columns is enough.
 */
internal val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        VERSION_2_TO_VERSION_3.forEach { db.execSQL(it) }
    }

    override fun migrate(connection: SQLiteConnection) {
        VERSION_2_TO_VERSION_3.forEach { connection.execSQL(it) }
    }
}

private val VERSION_2_TO_VERSION_3 = listOf(
    "ALTER TABLE `bolus_data` ADD COLUMN `origin` TEXT",
    "ALTER TABLE `insulin_data` ADD COLUMN `origin` TEXT",
)

/**
 * Version 4: cbg gains an origin, a payload and a deviceId; food gains a payload; bolus gains an
 * expectedNormal and its deliveryContext becomes nullable (SQLite can't alter a column's
 * nullability in place, so bolus_data is rebuilt: a new table with the final shape, its rows
 * copied over, the old table dropped, the new one renamed). dosingDecision gains an origin and a
 * payload. The three settings tables (cgmSettings, controllerSettings, pumpSettings) are dropped
 * and recreated with their real columns, since they've never been drained and have nothing worth
 * keeping; pumpSettings loses insulinFormulation, automatedDelivery and serialNumber, and
 * cgmSettings loses serialNumber (TidepoolKit never set them: insulinFormulation is an object
 * TidepoolKit never sends, and TidepoolKit#75 removed the other three). smbg, pumpStatus and
 * controllerStatus get new tables.
 *
 * recommendedBasal.duration also changed from a Double number of minutes to a Long number of
 * milliseconds: after the SQL runs, [rewriteRecommendedBasalDurationsToMilliseconds] rewrites
 * every already-queued dosing decision's stored JSON so it keeps decoding.
 */
internal val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        VERSION_3_TO_VERSION_4.forEach { db.execSQL(it) }
        rewriteRecommendedBasalDurationsToMilliseconds(
            selectRows = {
                db.query(SELECT_RECOMMENDED_BASAL_SQL).use { cursor ->
                    buildList { while (cursor.moveToNext()) add(cursor.getString(0) to cursor.getString(1)) }
                }
            },
            updateRow = { id, json -> db.execSQL(UPDATE_RECOMMENDED_BASAL_SQL, arrayOf(json, id)) },
        )
    }

    override fun migrate(connection: SQLiteConnection) {
        VERSION_3_TO_VERSION_4.forEach { connection.execSQL(it) }
        rewriteRecommendedBasalDurationsToMilliseconds(
            selectRows = {
                connection.prepare(SELECT_RECOMMENDED_BASAL_SQL).use { statement ->
                    buildList { while (statement.step()) add(statement.getText(0) to statement.getText(1)) }
                }
            },
            updateRow = { id, json ->
                connection.prepare(UPDATE_RECOMMENDED_BASAL_SQL).use { statement ->
                    statement.bindText(1, json)
                    statement.bindText(2, id)
                    statement.step()
                }
            },
        )
    }
}

private val VERSION_3_TO_VERSION_4 = listOf(
    "ALTER TABLE `continuous_glucose_data` ADD COLUMN `origin` TEXT",
    "ALTER TABLE `continuous_glucose_data` ADD COLUMN `payload` TEXT",
    "ALTER TABLE `continuous_glucose_data` ADD COLUMN `device_id` TEXT",
    "ALTER TABLE `food_data` ADD COLUMN `payload` TEXT",
    "ALTER TABLE `dosing_decision_data` ADD COLUMN `origin` TEXT",
    "ALTER TABLE `dosing_decision_data` ADD COLUMN `payload` TEXT",
    "CREATE TABLE IF NOT EXISTS `bolus_data_new` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `sub_type` TEXT NOT NULL, `delivery_context` TEXT, `normal` REAL, `expected_normal` REAL, `origin` TEXT, PRIMARY KEY(`id`))",
    "INSERT INTO `bolus_data_new` (`id`, `type`, `time`, `annotations`, `associations`, `clock_drift_offset`, `conversion_offset`, `data_set_id`, `device_time`, `notes`, `time_zone`, `time_zone_offset`, `sub_type`, `delivery_context`, `normal`, `origin`) " +
        "SELECT `id`, `type`, `time`, `annotations`, `associations`, `clock_drift_offset`, `conversion_offset`, `data_set_id`, `device_time`, `notes`, `time_zone`, `time_zone_offset`, `sub_type`, `delivery_context`, `normal`, `origin` FROM `bolus_data`",
    "DROP TABLE `bolus_data`",
    "ALTER TABLE `bolus_data_new` RENAME TO `bolus_data`",
    "DROP TABLE IF EXISTS `cgm_settings_data`",
    "CREATE TABLE IF NOT EXISTS `cgm_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `firmware_version` TEXT, `hardware_version` TEXT, `manufacturers` TEXT, `model` TEXT, `name` TEXT, `software_version` TEXT, `transmitter_id` TEXT, `units` TEXT, `origin` TEXT, `payload` TEXT, PRIMARY KEY(`id`))",
    "DROP TABLE IF EXISTS `controller_settings_data`",
    "CREATE TABLE IF NOT EXISTS `controller_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `device` TEXT, `notifications` TEXT, `origin` TEXT, `payload` TEXT, PRIMARY KEY(`id`))",
    "DROP TABLE IF EXISTS `pump_settings_data`",
    "CREATE TABLE IF NOT EXISTS `pump_settings_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `active_schedule_name` TEXT, `basal` TEXT, `basal_rate_schedules` TEXT, `blood_glucose_safety_limit` REAL, `blood_glucose_target_physical_activity` TEXT, `blood_glucose_target_preprandial` TEXT, `blood_glucose_target_schedules` TEXT, `bolus` TEXT, `carbohydrate_ratio_schedules` TEXT, `display` TEXT, `firmware_version` TEXT, `hardware_version` TEXT, `insulin_model` TEXT, `insulin_sensitivity_schedules` TEXT, `manufacturers` TEXT, `model` TEXT, `name` TEXT, `override_presets` TEXT, `schedule_time_zone_offset` INTEGER, `software_version` TEXT, `units` TEXT, `origin` TEXT, `payload` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `smbg_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `value` REAL, `units` TEXT, `sub_type` TEXT, `origin` TEXT, `payload` TEXT, `device_id` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `pump_status_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `basal_delivery` TEXT, `battery` TEXT, `bolus_delivery` TEXT, `delivery_indeterminant` INTEGER, `reservoir` TEXT, `origin` TEXT, `payload` TEXT, PRIMARY KEY(`id`))",
    "CREATE TABLE IF NOT EXISTS `controller_status_data` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `time` INTEGER, `annotations` TEXT, `associations` TEXT, `clock_drift_offset` INTEGER, `conversion_offset` INTEGER, `data_set_id` TEXT, `device_time` TEXT, `notes` TEXT, `time_zone` TEXT, `time_zone_offset` INTEGER, `battery` TEXT, `origin` TEXT, `payload` TEXT, PRIMARY KEY(`id`))",
)

private const val SELECT_RECOMMENDED_BASAL_SQL =
    "SELECT `id`, `recommended_basal` FROM `dosing_decision_data` WHERE `recommended_basal` IS NOT NULL"
private const val UPDATE_RECOMMENDED_BASAL_SQL =
    "UPDATE `dosing_decision_data` SET `recommended_basal` = ? WHERE `id` = ?"

/**
 * `dosing_decision_data.recommended_basal` stored `{"rate":..., "duration": <minutes, a Double>}`
 * before version 4; `DosingDecisionDataDto.RecommendedBasalDto.duration` is now milliseconds, as a
 * Long. Rewriting in Kotlin rather than with SQLite's `json_set` because JSON1 isn't guaranteed on
 * API 26. A row whose JSON can't be parsed, or whose `duration` isn't a number, is left alone
 * rather than failing the whole migration.
 */
private fun rewriteRecommendedBasalDurationsToMilliseconds(
    selectRows: () -> List<Pair<String, String>>,
    updateRow: (id: String, json: String) -> Unit,
) {
    selectRows().forEach { (id, json) ->
        rewriteDurationMinutesToMilliseconds(json)?.let { updateRow(id, it) }
    }
}

private fun rewriteDurationMinutesToMilliseconds(json: String): String? = runCatching {
    val recommendedBasal = Json.parseToJsonElement(json).jsonObject
    val durationMinutes = recommendedBasal["duration"]?.jsonPrimitive?.doubleOrNull ?: return@runCatching null
    val rewritten = JsonObject(
        recommendedBasal + ("duration" to JsonPrimitive((durationMinutes * MILLISECONDS_PER_MINUTE).roundToLong())),
    )
    Json.encodeToString(JsonObject.serializer(), rewritten)
}.getOrNull()

private const val MILLISECONDS_PER_MINUTE = 60_000

/** Every outbox migration, oldest first. The test opens outboxes with the same list. */
internal val OUTBOX_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)

actual fun provideAlertApi(ktorfit: Ktorfit) = ktorfit.createAlertApi()
actual fun provideAuthorizationApi(ktorfit: Ktorfit) = ktorfit.createAuthorizationApi()
actual fun provideBlobApi(ktorfit: Ktorfit) = ktorfit.createBlobApi()
actual fun provideClinicApi(ktorfit: Ktorfit) = ktorfit.createClinicApi()
actual fun provideConfirmationApi(ktorfit: Ktorfit) = ktorfit.createConfirmationApi()
actual fun provideDataApi(ktorfit: Ktorfit) = ktorfit.createDataApi()

//actual fun provideExportApi(ktorfit: Ktorfit) = ktorfit.createExportApi()
actual fun provideGeneralApi(ktorfit: Ktorfit) = ktorfit.createGeneralApi()
actual fun provideMessageApi(ktorfit: Ktorfit) = ktorfit.createMessageApi()
actual fun provideMetadataApi(ktorfit: Ktorfit) = ktorfit.createMetadataApi()
actual fun provideMetricsApi(ktorfit: Ktorfit) = ktorfit.createMetricsApi()
actual fun providePrescriptionApi(ktorfit: Ktorfit) = ktorfit.createPrescriptionApi()
actual fun provideSummaryApi(ktorfit: Ktorfit) = ktorfit.createSummaryApi()
actual fun provideTaskApi(ktorfit: Ktorfit) = ktorfit.createTaskApi()
actual fun provideUserApi(ktorfit: Ktorfit) = ktorfit.createUserApi()