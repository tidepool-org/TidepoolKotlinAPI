package org.tidepool.sdk.service

import kotlinx.serialization.json.JsonObject
import org.tidepool.sdk.TokenProvider
import org.tidepool.sdk.flatMap
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.metadata.MetadataCollection
import org.tidepool.sdk.model.metadata.UserProfile
import org.tidepool.sdk.model.metadata.toDomain
import org.tidepool.sdk.model.metadata.toDto
import org.tidepool.sdk.model.metadata.users.TrustUser
import org.tidepool.sdk.model.metadata.users.toDomain
import org.tidepool.sdk.repository.MetadataRepository
import org.tidepool.sdk.repository.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MetadataService internal constructor(
    private val metadataRepository: MetadataRepository,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    
    suspend fun getMetadataCollections(): Result<List<String>> =
        metadataRepository.getMetadataCollections(
            sessionToken = tokenProvider.getToken()
        )
    
    suspend fun getUserMetadataCollection(
        userId: String,
        collection: MetadataCollection,
    ): Result<JsonObject> =
        metadataRepository.getUserMetadataCollection(
            sessionToken = tokenProvider.getToken(),
            userId = userId,
            collectionName = collection.code,
        )
    
    suspend fun getCurrentUserMetadataCollection(
        collection: MetadataCollection,
    ): Result<JsonObject> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            metadataRepository.getUserMetadataCollection(
                sessionToken = token,
                userId = user.userId,
                collectionName = collection.code,
            )
        }
    }
    
    suspend fun updateUserMetadataCollection(
        userId: String,
        collectionName: String,
        collection: UserProfile,
        usePost: Boolean = false
    ): Result<JsonObject> =
        metadataRepository.updateUserMetadataCollection(
            sessionToken = tokenProvider.getToken(),
            userId = userId,
            collectionName = collectionName,
            collection = collection.toDto(),
            usePost = usePost
        )
    
    suspend fun updateCurrentUserMetadataCollection(
        collectionName: String,
        collection: UserProfile,
        usePost: Boolean = false
    ): Result<JsonObject> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            metadataRepository.updateUserMetadataCollection(
                sessionToken = token,
                userId = user.userId,
                collectionName = collectionName,
                collection = collection.toDto(),
                usePost = usePost
            )
        }
    }
    
    suspend fun getUserPrivateMetadataItem(
        userId: String,
        fieldName: String
    ): Result<JsonObject> =
        metadataRepository.getUserPrivateMetadataItem(
            sessionToken = tokenProvider.getToken(),
            userId = userId,
            fieldName = fieldName
        )
    
    suspend fun getTrustUsers(): Result<List<TrustUser>> =
        metadataRepository.getTrustUsers(
            sessionToken = tokenProvider.getToken(),
        ).mapList { dto -> dto.toDomain() }
    
    suspend fun getCurrentUserTrustUsers(): Result<List<TrustUser>> =
        tokenProvider.getToken().let { token ->
            userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
                metadataRepository.getTrustUsers(
                    sessionToken = token,
                )
            }
        }.mapList { dto -> dto.toDomain() }
    
    suspend fun getUserProfile(userId: String): Result<UserProfile> =
        metadataRepository.getUserProfile(
            sessionToken = tokenProvider.getToken(),
            userId = userId
        ).map { it.toDomain() }
    
    suspend fun getCurrentUserProfile(): Result<UserProfile> =
        tokenProvider.getToken().let { token ->
            userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
                metadataRepository.getUserProfile(
                    sessionToken = token,
                    userId = user.userId
                )
            }
        }.map { it.toDomain() }
    
    // Convenience methods for patient profiles
    
    suspend fun updateUserProfile(
        userId: String,
        profile: UserProfile,
        usePost: Boolean = false
    ): Result<JsonObject> = metadataRepository.updateUserMetadataCollection(
        sessionToken = tokenProvider.getToken(),
        userId = userId,
        collectionName = MetadataCollection.Profile.code,
        collection = profile.toDto(),
        usePost = usePost
    )
    
    suspend fun updateCurrentUserProfile(
        profile: UserProfile,
        usePost: Boolean = false
    ): Result<JsonObject> = tokenProvider.getToken().let { token ->
        userRepository.getCurrentUser(sessionToken = token).flatMap { user ->
            updateUserProfile(
                userId = user.userId,
                profile = profile,
                usePost = usePost
            )
        }
    }
    
    suspend fun createPatientProfile(
        fullName: String? = null,
        diagnosisType: UserProfile.DiagnosisType? = null,
        diagnosisDate: LocalDate? = null,
        birthday: LocalDate? = null,
        biologicalSex: UserProfile.BiologicalSex? = null,
        targetDevices: List<String>? = null,
        targetTimezone: String? = null,
        about: String? = null,
    ): UserProfile = UserProfile(
        fullName = fullName,
        patient = UserProfile.PatientData(
            diagnosisType = diagnosisType,
            diagnosisDate = diagnosisDate?.format(DateTimeFormatter.ISO_LOCAL_DATE),
            birthday = birthday?.format(DateTimeFormatter.ISO_LOCAL_DATE),
            biologicalSex = biologicalSex,
            targetDevices = targetDevices,
            targetTimezone = targetTimezone,
            about = about,
        )
    )
    
    // Additional convenience methods for patient data
    
    suspend fun updateCurrentUserPatientData(
        diagnosisType: UserProfile.DiagnosisType? = null,
        diagnosisDate: LocalDate? = null,
        birthday: LocalDate? = null,
        biologicalSex: UserProfile.BiologicalSex? = null,
        targetDevices: List<String>? = null,
        targetTimezone: String? = null,
        about: String? = null,
        usePost: Boolean = false,
    ) = getCurrentUserProfile().flatMap { currentProfile ->
        val updatedProfile = currentProfile.copy(
            patient = UserProfile.PatientData(
                diagnosisType = diagnosisType ?: currentProfile.patient?.diagnosisType,
                diagnosisDate = diagnosisDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    ?: currentProfile.patient?.diagnosisDate,
                birthday = birthday?.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    ?: currentProfile.patient?.birthday,
                biologicalSex = biologicalSex ?: currentProfile.patient?.biologicalSex,
                targetDevices = targetDevices ?: currentProfile.patient?.targetDevices,
                targetTimezone = targetTimezone ?: currentProfile.patient?.targetTimezone,
                about = about ?: currentProfile.patient?.about,
            )
        )
        updateCurrentUserProfile(updatedProfile, usePost)
    }
}