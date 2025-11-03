[api-docs]: <https://tidepool.stoplight.io/docs/tidepool-api/7ef2485b09b04-welcome> "Tidepool Stoplight Docs"

[example]: <https://github.com/tidepool-org/AndroidCarePartner> "Android Care Partner Application"

[swift-api]: <https://github.com/tidepool-org/TidepoolKit> "Swift Library for Backend Communication"

# TidepoolKotlinAPI

A Kotlin library for interacting with the Tidepool API.

## Features

- **Dependency Injection**: Uses Koin for clean dependency management
- **Environment Support**: Easy switching between Production, Integration, Dev, and QA environments
- **Kotlinx Serialization**: Type-safe JSON handling with polymorphic serialization
- **Coroutines**: Full support for Kotlin coroutines and suspend functions
- **Repository Pattern**: Clean separation between API and business logic
- **Token Management**: Token-based authentication with flexible token provider

## Quick Start

```kotlin
val sdk = TidepoolSDK(
    environment = Environments.Production,
    getToken = { 
        // Provide your token retrieval logic
        "your-session-token"
    }
)

// Get current user information
val userResult = sdk.getCurrentUserInfo()
userResult.fold(
    onSuccess = { user -> println("User: ${user.username}") },
    onFailure = { error -> println("Error: ${error.message}") }
)

// Get diabetes data for a user
val dataResult = sdk.getUserData(
    userId = "user-id",
    types = listOf(DataType.Cbg, DataType.Bolus),
    startDate = Instant.now().minus(Duration.ofDays(7)),
    endDate = Instant.now()
)

// Get care partner invitations
val invitationsResult = sdk.getReceivedInvitations()

// Get trust relationships (care partners)
val trustUsersResult = sdk.getTrustUsers()

// Clean up when done
sdk.shutdown()
```

## Architecture

This library is structured in multiple modules:

- **`lib`** - Main SDK module with public API and domain models
- **`data`** - Data layer with repository implementations, DTOs, and Retrofit API interfaces (internal)

The SDK follows Clean Architecture principles with clear separation between domain models (in `lib`) and data transfer
objects (in `data`).

## Supported Data Types

The SDK currently supports the following Tidepool data types:

- Continuous Glucose (CGM/CBG)
- Bolus insulin doses
- Basal insulin (automated)
- Food entries
- Dosing decisions
- Insulin data

## Environment Configuration

Available environments:

- `Environments.Production` - Production Tidepool environment
- `Environments.Integration` - Integration testing environment
- `Environments.Dev1` - Development environment
- `Environments.Qa1` - QA environment 1
- `Environments.Qa2` - QA environment 2

## Example

See the [example application][example].

## Other languages

* [Swift (TidepoolKit)][swift-api]

