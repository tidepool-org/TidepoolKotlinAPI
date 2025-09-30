package org.tidepool.sdk

import retrofit2.HttpException

inline fun <T : Any> runCatchingNetworkExceptions(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (ex: HttpException) {
    Result.failure(ex)
}
