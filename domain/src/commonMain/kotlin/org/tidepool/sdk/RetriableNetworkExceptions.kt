package org.tidepool.sdk

import kotlin.reflect.KClass

/**
 * Network failure types considered transient and safe to retry. Shared by every caller that
 * needs to distinguish a retriable failure from a terminal one - e.g.
 * [org.tidepool.sdk.runWithRetry]'s default and `DataUploadWorker` in the app module - so the
 * retriable set can't drift between callers.
 *
 * Deliberately not declared in `NetworkExceptions.kt`: the `data` module has its own file of
 * that same name in this same package (`org.tidepool.sdk`), and top-level declarations compile
 * to a per-file facade class named after the file (`NetworkExceptionsKt`). Two same-named
 * facade classes on the same runtime classpath collide - whichever module's class the
 * classloader resolves first wins, silently dropping the other file's declarations. A separate
 * file name keeps this property's facade class unique.
 */
val RetriableNetworkExceptions: List<KClass<out TidepoolNetworkException>> = listOf(
    NetworkUnavailableException::class,
    TimeoutException::class,
    GenericNetworkException::class,
    NoInternetException::class,
    InternalServerErrorException::class,
    BadGatewayException::class,
    ServiceUnavailableException::class,
    GatewayTimeoutException::class,
)
