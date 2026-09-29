package org.tidepool.sdk

import android.content.Context
import org.koin.dsl.module
import org.tidepool.sdk.fake.FakeBackendRecorder

/**
 * @param fakeBackendRecorder turns on the developer-only fake backend: requests to
 * [Environment.Fake] are answered in-process and handed to it. Leave null in release builds.
 */
public fun TidepoolSDK.Companion.create(
    tokenProvider: TokenProvider,
    context: Context,
    fakeBackendRecorder: FakeBackendRecorder? = null,
) = TidepoolSDK(
    tokenProvider = tokenProvider,
    platformModule = module {
        single<Context> { context }
        fakeBackendRecorder?.let { recorder -> single<FakeBackendRecorder> { recorder } }
    },
)
