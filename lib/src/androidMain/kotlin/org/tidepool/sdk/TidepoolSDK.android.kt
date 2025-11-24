package org.tidepool.sdk

import android.content.Context
import org.koin.dsl.module

public fun TidepoolSDK.Companion.create(
    tokenProvider: TokenProvider,
    context: Context,
) = TidepoolSDK(
    tokenProvider = tokenProvider,
    platformModule = module {
        single<Context> { context }
    },
)