package org.tidepool.sdk.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
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
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import org.tidepool.sdk.AndroidKeyValueStorage
import org.tidepool.sdk.DnsResolver
import org.tidepool.sdk.DnsResolverImpl
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
    }

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE bolus_data ADD COLUMN normal REAL")
    }

    override fun migrate(connection: SQLiteConnection) {
        val statement = connection.prepare("ALTER TABLE bolus_data ADD COLUMN normal REAL")
        try {
            statement.step()
        } finally {
            statement.close()
        }
    }
}


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