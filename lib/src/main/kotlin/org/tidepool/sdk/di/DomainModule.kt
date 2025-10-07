package org.tidepool.sdk.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.tidepool.sdk.service.ClinicService
import org.tidepool.sdk.service.ConfirmationService
import org.tidepool.sdk.service.DataService
import org.tidepool.sdk.service.MetadataService
import org.tidepool.sdk.service.UserService

val domainModule = module {
    singleOf(::ClinicService)
    singleOf(::ConfirmationService)
    singleOf(::DataService)
    singleOf(::MetadataService)
    singleOf(::UserService)
}