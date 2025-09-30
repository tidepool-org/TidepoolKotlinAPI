package org.tidepool.sdk

import kotlinx.coroutines.runBlocking
import org.tidepool.sdk.dto.auth.RealmDto
import org.tidepool.sdk.dto.auth.TokenRequestDto
import java.time.Instant
import kotlin.time.Duration.Companion.days

/**
 * Example usage of the Tidepool Data Module
 *
 * This demonstrates the typical flow:
 * 1. Initialize NetworkModule
 * 2. Authenticate with Tidepool
 * 3. Fetch user data
 * 4. Fetch diabetes data
 */
//fun main() = runBlocking {
//    // Initialize the data module
//    val networkModule = NetworkModule(
//        baseUrl = "https://api.tidepool.org",
//        authUrl = "https://auth.tidepool.org"
//    )
//
//    // Step 1: Authentication
//    println("🔐 Authenticating with Tidepool...")
//
//    val tokenRequest = TokenRequestDto.createWithPassword(
//        client_id = "your-client-id",
//        username = "user@example.com",
//        password = "your-password"
//    )
//
//    val authResult = networkModule.authRepository.obtainToken(
//        realm = RealmDto.Tidepool,
//        tokenRequest = tokenRequest
//    )
//
//    val sessionToken = authResult.fold(
//        onSuccess = { tokenResponse ->
//            println("✅ Authentication successful!")
//            println("   Access token: ${tokenResponse.access_token.take(20)}...")
//            println("   Expires in: ${tokenResponse.expires_in} seconds")
//            tokenResponse.access_token
//        },
//        onFailure = { error ->
//            println("❌ Authentication failed: ${error.message}")
//            return@runBlocking
//        }
//    )
//
//    // Step 2: Get user information
//    println("\n👤 Fetching user information...")
//
//    val userResult = networkModule.userRepository.getUser(
//        sessionToken = sessionToken,
//        userId = "user-id-here"
//    )
//
//    userResult.fold(
//        onSuccess = { user ->
//            println("✅ User fetched successfully!")
//            println("   Username: ${user.username}")
//            println("   Email verified: ${user.emailVerified}")
//        },
//        onFailure = { error ->
//            println("❌ Failed to fetch user: ${error.message}")
//        }
//    )
//
//    // Step 3: Fetch diabetes data
//    println("\n📊 Fetching diabetes data...")
//
//    val dataResult = networkModule.dataRepository.getDataForUser(
//        sessionToken = sessionToken,
//        userId = "user-id-here",
//        types = arrayOf(
//            DataTypeDto.Bolus,      // Insulin boluses
//            DataTypeDto.Cbg,        // Continuous glucose readings
//            DataTypeDto.Food        // Food entries
//        ),
//        startDate = Instant.now().minusSeconds(7.days.inWholeSeconds),
//        endDate = Instant.now(),
//        latest = false
//    )
//
//    dataResult.fold(
//        onSuccess = { dataArray ->
//            println("✅ Data fetched successfully!")
//            println("   Total data points: ${dataArray.size}")
//
//            // Group by type
//            val groupedData = dataArray.groupBy { it.type }
//            groupedData.forEach { (type, data) ->
//                println("   - $type: ${data.size} entries")
//            }
//        },
//        onFailure = { error ->
//            println("❌ Failed to fetch data: ${error.message}")
//        }
//    )
//
//    println("\n🎉 Example completed!")
//}