package org.tidepool.sdk.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.execSQL
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
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

actual val platformDataModule: Module
    get() = module {
        single {
            Room.databaseBuilder<LoopKitDatabase>(
                context = get(),
                name = "loop-kit-database",
            )
                .setDriver(BundledSQLiteDriver())
                .addMigrations(MIGRATION_1_2)
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

/** Version 2's tables as `schemas/.../2.json` records them. Frozen: later versions migrate from these. */
private val VERSION_2_TABLES = listOf(
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