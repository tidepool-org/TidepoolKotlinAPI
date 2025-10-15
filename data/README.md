# Tidepool Kotlin API - Data Module

This module contains the data layer implementation for the Tidepool Kotlin API, following Clean
Architecture principles with modern Android development practices.

## Structure

```
data/
├── src/main/kotlin/org/tidepool/sdk/
│   ├── api/                    # Retrofit API interfaces
│   │   ├── AuthApi.kt          # Authentication endpoints
│   │   ├── ConfirmationApi.kt  # Email/phone confirmation endpoints
│   │   ├── DataApi.kt          # Diabetes data endpoints
│   │   ├── MetadataApi.kt      # Metadata endpoints
│   │   └── UserApi.kt          # User management endpoints
│   ├── dto/                    # Data Transfer Objects
│   │   ├── auth/               # Authentication DTOs
│   │   ├── confirmation/       # Email/phone confirmation DTOs
│   │   ├── data/               # Diabetes data DTOs (bolus, CGM, food, etc.)
│   │   ├── metadata/           # Metadata DTOs
│   │   └── user/               # User management DTOs
│   ├── repository/             # Repository interfaces & implementations
│   │   ├── AuthRepository.kt
│   │   ├── ConfirmationRepository.kt
│   │   ├── DataRepository.kt
│   │   ├── MetadataRepository.kt
│   │   ├── UserRepository.kt
│   │   └── impl/               # Repository implementations
│   ├── deserialization/        # JSON serialization utilities
│   ├── di/                     # Dependency injection modules
│   │   └── DataModule.kt       # Koin module configuration
│   └── datasource/             # Data source implementations
```

## Key Features

- **Clean Architecture**: Separation of concerns with repository pattern
- **Dependency Injection**: Koin-based DI for easy testing and modularity
- **Type Safety**: Comprehensive DTO classes with Kotlin serialization
- **Async Operations**: Coroutines-based async/await pattern
- **Error Handling**: Result types for safe error handling
- **Polymorphic Serialization**: Support for different diabetes data types

## Dependencies

This module leverages modern Android/Kotlin libraries:

- **Retrofit 3.0.0** - HTTP networking with coroutines support
- **Kotlinx Serialization** - JSON parsing with polymorphic support
- **Koin 4.1.0** - Dependency injection
- **OkHttp 5.1.0** - HTTP client with logging interceptor
- **Coroutines 1.10.2** - Async operations

## Setup

### 1. Initialize Dependencies

```kotlin
// Initialize Koin DI
startKoin {
    modules(dataModule)
}

// Provide environment configuration
val environment = EnvironmentInternal(
    url = "https://api.tidepool.org",
    auth = AuthenticationServerInternal("https://auth.tidepool.org")
)
```

### 2. Inject Repositories

```kotlin
// Get repositories from Koin
val authRepository: AuthRepository by inject()
val dataRepository: DataRepository by inject()
val userRepository: UserRepository by inject()
val confirmationRepository: ConfirmationRepository by inject()
val metadataRepository: MetadataRepository by inject()
```

## Usage Examples

### Authentication Flow

```kotlin
// Password-based authentication
val tokenRequest = TokenRequestDto.createWithPassword(
    client_id = "your-client-id",
    username = "user@example.com", 
    password = "password"
)

val tokenResult = authRepository.obtainToken(
    realm = RealmDto.Tidepool,
    tokenRequest = tokenRequest
)

tokenResult.fold(
    onSuccess = { tokenResponse ->
        val sessionToken = tokenResponse.access_token
        println("Authenticated successfully")
    },
    onFailure = { error ->
        println("Authentication failed: ${error.message}")
    }
)
```

### Email Confirmation

```kotlin
// Send confirmation email
val confirmationResult = confirmationRepository.sendConfirmation(
    sessionToken = sessionToken,
    type = ConfirmationType.SIGNUP,
    emailAddress = "user@example.com"
)

// Confirm email with verification code
val confirmResult = confirmationRepository.confirmSignup(
    confirmationKey = "verification-key-from-email"
)
```

### Fetching Diabetes Data

```kotlin
// Fetch specific data types for a date range
val dataResult = dataRepository.getDataForUser(
    sessionToken = sessionToken,
    userId = "user-id",
    types = CommaSeparatedArray(
        BaseDataDto.DataTypeDto.Bolus,
        BaseDataDto.DataTypeDto.Cbg,
        BaseDataDto.DataTypeDto.Food
    ),
    startDate = Instant.now().minus(Duration.ofDays(7)),
    endDate = Instant.now()
)

dataResult.fold(
    onSuccess = { dataList ->
        // Process different data types polymorphically
        dataList.forEach { baseData ->
            when (baseData) {
                is BolusDataDto -> println("Insulin bolus: ${baseData.normal}u")
                is ContinuousGlucoseDataDto -> println("BG: ${baseData.value} mg/dL")
                is FoodDataDto -> println("Carbs: ${baseData.nutrition?.carbohydrate?.net}g")
                // Handle other data types...
            }
        }
    },
    onFailure = { error ->
        println("Failed to fetch data: ${error.message}")
    }
)
```

### User Management

```kotlin
// Get user profile
val userResult = userRepository.getUser(
    sessionToken = sessionToken,
    userId = userId
)

// Get user metadata
val metadataResult = metadataRepository.getMetadataForUser(
    sessionToken = sessionToken,
    userId = userId
)
```

## Data Types Supported

The module supports comprehensive diabetes data types:

- **BolusDataDto** - Insulin bolus data
- **BasalAutomatedDataDto** - Automated basal insulin
- **ContinuousGlucoseDataDto** - CGM readings
- **FoodDataDto** - Food and carbohydrate entries
- **DosingDecisionDataDto** - Automated insulin dosing decisions
- **InsulinDataDto** - General insulin data

## Error Handling

All operations return `Result<T>` types for explicit error handling:

```kotlin
when (val result = repository.someOperation()) {
    is Result.Success -> {
        val data = result.getOrNull()
        // Handle success
    }
    is Result.Failure -> {
        val error = result.exceptionOrNull()
        // Handle error
    }
}
```

## Testing

The module includes comprehensive test coverage with:

- **JUnit 5** for unit tests
- **Coroutines Test** for async testing
- **MockWebServer** for API mocking
- **Koin Test** for DI testing

Run tests with:

```bash
./gradlew :data:test
```

## Architecture Benefits

- **Testability**: Dependency injection enables easy mocking
- **Maintainability**: Clear separation of concerns
- **Scalability**: Repository pattern supports multiple data sources
- **Type Safety**: Kotlin's type system prevents runtime errors
- **Modern**: Uses latest Android/Kotlin best practices