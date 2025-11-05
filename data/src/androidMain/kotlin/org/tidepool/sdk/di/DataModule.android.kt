package org.tidepool.sdk.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import org.tidepool.sdk.database.LoopKitDatabase
import org.tidepool.sdk.api.AlertApi
import org.tidepool.sdk.api.AuthorizationApi
import org.tidepool.sdk.api.BlobApi
import org.tidepool.sdk.api.ClinicApi
import org.tidepool.sdk.api.ConfirmationApi
import org.tidepool.sdk.api.DataApi
import org.tidepool.sdk.api.GeneralApi
import org.tidepool.sdk.api.MessageApi
import org.tidepool.sdk.api.MetadataApi
import org.tidepool.sdk.api.MetricsApi
import org.tidepool.sdk.api.PrescriptionApi
import org.tidepool.sdk.api.SummaryApi
import org.tidepool.sdk.api.TaskApi
import org.tidepool.sdk.api.UserApi
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

actual val platformDataModule: Module
    get() = module {
        single {
            Room.databaseBuilder<LoopKitDatabase>(
                context = get(),
                name = "loop-kit-database",
            )
                .setDriver(BundledSQLiteDriver())
                .build()
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