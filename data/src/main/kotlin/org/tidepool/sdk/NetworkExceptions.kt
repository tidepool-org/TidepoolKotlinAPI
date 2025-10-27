package org.tidepool.sdk

import retrofit2.HttpException

/**
 * Helper function to map HttpException to specific Tidepool network exceptions
 */
fun HttpException.toTidepoolException() = when (code()) {
    400  -> BadRequestException("Bad request: ${message()}", this)
    401  -> UnauthorizedException("Unauthorized: ${message()}", this)
    403  -> ForbiddenException("Forbidden: ${message()}", this)
    404  -> NotFoundException("Not found: ${message()}", this)
    409  -> ConflictException("Conflict: ${message()}", this)
    422  -> ValidationException("Validation error: ${message()}", this)
    429  -> TooManyRequestsException("Too many requests: ${message()}", this)
    500  -> InternalServerErrorException("Internal server error: ${message()}", this)
    502  -> BadGatewayException("Bad gateway: ${message()}", this)
    503  -> ServiceUnavailableException("Service unavailable: ${message()}", this)
    504  -> GatewayTimeoutException("Gateway timeout: ${message()}", this)
    else -> UnknownNetworkException("HTTP ${code()}: ${message()}", this)
}