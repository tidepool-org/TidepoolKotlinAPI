package org.tidepool.sdk

import android.content.Context
import org.koin.dsl.module

public fun TidepoolSDK.Companion.create(
    environment: Environment,
    tokenProvider: TokenProvider,
    context: Context,
) = TidepoolSDK(
    environment = environment,
    tokenProvider = tokenProvider,
    platformModule = module {
        single<Context> { context }
    },
)