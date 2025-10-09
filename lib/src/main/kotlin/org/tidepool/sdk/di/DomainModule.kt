package org.tidepool.sdk.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tidepool.sdk.service.BlobService
import org.tidepool.sdk.service.AlertService
import org.tidepool.sdk.service.AuthenticationService
import org.tidepool.sdk.service.AuthorizationService
import org.tidepool.sdk.service.ClinicService
import org.tidepool.sdk.service.ConfirmationService
import org.tidepool.sdk.service.DataService
import org.tidepool.sdk.service.ExportService
import org.tidepool.sdk.service.GeneralService
import org.tidepool.sdk.service.MessageService
import org.tidepool.sdk.service.MetadataService
import org.tidepool.sdk.service.SummaryService
import org.tidepool.sdk.service.UserService

val domainModule = module {
    singleOf(::AlertService)
    singleOf(::AuthenticationService)
    singleOf(::AuthorizationService)
    singleOf(::BlobService)
    singleOf(::ClinicService)
    singleOf(::ConfirmationService)
    singleOf(::DataService)
    singleOf(::ExportService)
    singleOf(::GeneralService)
    singleOf(::MessageService)
    singleOf(::MetadataService)
    singleOf(::SummaryService)
    singleOf(::UserService)
}