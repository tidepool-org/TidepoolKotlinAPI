package org.tidepool.sdk

import android.content.Context
import org.koin.dsl.module
import org.tidepool.sdk.di.VerboseHttpLogging
import org.tidepool.sdk.fake.FakeBackendRecorder

/**
 * @param fakeBackendRecorder turns on the developer-only fake backend: requests to
 * [Environment.Fake] are answered in-process and handed to it. Leave null in release builds.
 * @param verboseHttpLogging logs full request and response bodies, which hold health data.
 * Leave false in release builds. The session token is masked either way.
 */
public fun TidepoolSDK.Companion.create(
    tokenProvider: TokenProvider,
    context: Context,
    fakeBackendRecorder: FakeBackendRecorder? = null,
    verboseHttpLogging: Boolean = false,
) = TidepoolSDK(
    tokenProvider = tokenProvider,
    platformModule = module {
        single<Context> { context }
        single(VerboseHttpLogging) { verboseHttpLogging }
        fakeBackendRecorder?.let { recorder -> single<FakeBackendRecorder> { recorder } }
    },
)
