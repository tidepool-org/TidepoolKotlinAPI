package org.tidepool.sdk

/**
 * Base class for all Tidepool network-related exceptions
 */
sealed class TidepoolNetworkException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Authentication and authorization related exceptions
 */
sealed class AuthException(
    message: String,
    cause: Throwable? = null
) : TidepoolNetworkException(message, cause)

class UnauthorizedException(
    message: String = "Authentication required",
    cause: Throwable? = null
) : AuthException(message, cause)

class ForbiddenException(
    message: String = "Access denied",
    cause: Throwable? = null
) : AuthException(message, cause)

class TokenExpiredException(
    message: String = "Access token has expired",
    cause: Throwable? = null
) : AuthException(message, cause)

/**
 * Client error exceptions (4xx)
 */
sealed class ClientException(
    message: String,
    cause: Throwable? = null
) : TidepoolNetworkException(message, cause)

class BadRequestException(
    message: String = "Invalid request",
    cause: Throwable? = null
) : ClientException(message, cause)

class NotFoundException(
    message: String = "Resource not found",
    cause: Throwable? = null
) : ClientException(message, cause)

class ConflictException(
    message: String = "Resource conflict",
    cause: Throwable? = null
) : ClientException(message, cause)

class ValidationException(
    message: String = "Request validation failed",
    cause: Throwable? = null
) : ClientException(message, cause)

class TooManyRequestsException(
    message: String = "Rate limit exceeded",
    cause: Throwable? = null
) : ClientException(message, cause)

/**
 * Server error exceptions (5xx)
 */
sealed class ServerException(
    message: String,
    cause: Throwable? = null
) : TidepoolNetworkException(message, cause)

class InternalServerErrorException(
    message: String = "Internal server error",
    cause: Throwable? = null
) : ServerException(message, cause)

class BadGatewayException(
    message: String = "Bad gateway",
    cause: Throwable? = null
) : ServerException(message, cause)

class ServiceUnavailableException(
    message: String = "Service temporarily unavailable",
    cause: Throwable? = null
) : ServerException(message, cause)

class GatewayTimeoutException(
    message: String = "Gateway timeout",
    cause: Throwable? = null
) : ServerException(message, cause)

/**
 * Network connectivity exceptions
 */
sealed class NetworkException(
    message: String,
    cause: Throwable? = null
) : TidepoolNetworkException(message, cause)

class NoInternetException(
    message: String = "No internet connection available",
    cause: Throwable? = null
) : NetworkException(message, cause)

class TimeoutException(
    message: String = "Request timeout",
    cause: Throwable? = null
) : NetworkException(message, cause)

class NetworkUnavailableException(
    message: String = "Network is unavailable",
    cause: Throwable? = null
) : NetworkException(message, cause)

class GenericNetworkException(
    message: String = "Network error occurred",
    cause: Throwable? = null
) : NetworkException(message, cause)

/**
 * Unknown or generic network exception
 */
class UnknownNetworkException(
    message: String = "Unknown network error occurred",
    cause: Throwable? = null
) : TidepoolNetworkException(message, cause)