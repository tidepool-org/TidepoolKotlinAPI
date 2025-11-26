package org.tidepool.sdk

import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

/**
 * Extension function that wraps network calls and maps various network exceptions
 * to specific Tidepool network exception types for better error handling.
 *
 * This function catches and maps:
 * - HttpException: Maps to specific exception based on HTTP status code
 * - UnknownHostException: Maps to NoInternetException
 * - ConnectException: Maps to NetworkUnavailableException
 * - SocketTimeoutException: Maps to TimeoutException
 * - IOException: Maps to GenericNetworkException
 * - Other exceptions: Passed through as-is
 *
 * @param block The network operation to execute
 * @return Result<T> containing either success value or mapped exception
 */
suspend fun <T : Any> runCatchingNetworkExceptions(
    block: suspend () -> T
): Result<T> = try {
    Result.success(block())
} catch (ex: ResponseException) {
    // Map HTTP exceptions to specific Tidepool exceptions
    Result.failure(ex.toTidepoolException())
} catch (ex: UnknownHostException) {
    // DNS resolution failed or no internet connection
    Result.failure(NoInternetException("Unable to resolve host: ${ex.message}", ex))
} catch (ex: ConnectException) {
    // Connection refused or network unreachable
    Result.failure(NetworkUnavailableException("Connection failed: ${ex.message}", ex))
} catch (ex: SocketTimeoutException) {
    // Request timeout
    Result.failure(TimeoutException("Request timed out: ${ex.message}", ex))
} catch (ex: IOException) {
    // Other network I/O errors
    Result.failure(GenericNetworkException("Network I/O error: ${ex.message}", ex))
} catch (ex: Exception) {
    // Fallback for any other exceptions
    Result.failure(ex)
}

suspend fun <T : Any> runWithRetry(
    maxRetries: Int = 3,
    delay: Long = 2.seconds.inWholeMilliseconds,
    retriableExceptions: List<KClass<out TidepoolNetworkException>> = listOf(
        NetworkUnavailableException::class,
        TimeoutException::class,
        GenericNetworkException::class,
        NoInternetException::class,
        InternalServerErrorException::class,
        BadGatewayException::class,
        ServiceUnavailableException::class,
        GatewayTimeoutException::class,
    ),
    block: suspend () -> T,
): Result<T> = runCatchingNetworkExceptions(block)
    .fold(
        onSuccess = { Result.success(it) },
        onFailure = {
            if (maxRetries > 0 && it::class in retriableExceptions) {
                val jitteredDelay = delay + Random.nextLong(-delay / 10, delay / 10)

                delay(jitteredDelay)

                runWithRetry(
                    block = block,
                    maxRetries = maxRetries - 1,
                    delay = delay,
                    retriableExceptions = retriableExceptions,
                )
            } else {
                Result.failure(it)
            }
        },
    )
