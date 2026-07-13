package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import org.tidepool.sdk.api.PrescriptionApi
import org.tidepool.sdk.di.providePrescriptionApi
import org.tidepool.sdk.dto.prescription.ClaimPrescriptionDto
import org.tidepool.sdk.dto.prescription.toDomain
import org.tidepool.sdk.dto.prescription.toDto
import org.tidepool.sdk.dto.prescription.toClaimedPrescription
import org.tidepool.sdk.dto.prescription.toInitialSettings
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.prescription.ClaimedPrescription
import org.tidepool.sdk.model.prescription.InitialSettings
import org.tidepool.sdk.model.prescription.NewPrescription
import org.tidepool.sdk.model.prescription.Prescription
import org.tidepool.sdk.model.prescription.UpdatePrescription
import org.tidepool.sdk.runCatchingNetworkExceptions

class PrescriptionRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
) : PrescriptionRepository {

    private val prescriptionApi: PrescriptionApi
        get() = providePrescriptionApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun createPrescription(
        sessionToken: String,
        newPrescription: NewPrescription,
    ): Result<Prescription> = runCatchingNetworkExceptions {
        prescriptionApi.createPrescription(
            sessionToken = sessionToken,
            requestBody = newPrescription.toDto(),
        )
    }.map { it.toDomain() }

    override suspend fun getPrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<Prescription> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
        )
    }.map { it.toDomain() }

    override suspend fun updatePrescription(
        sessionToken: String,
        prescriptionId: String,
        updatePrescription: UpdatePrescription,
    ): Result<Prescription> = runCatchingNetworkExceptions {
        prescriptionApi.updatePrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
            requestBody = updatePrescription.toDto(),
        )
    }.map { it.toDomain() }

    override suspend fun deletePrescription(
        sessionToken: String,
        prescriptionId: String,
    ): Result<Unit> = runCatchingNetworkExceptions {
        prescriptionApi.deletePrescription(
            sessionToken = sessionToken,
            prescriptionId = prescriptionId,
        )
    }

    override suspend fun getPrescriptionsForPatient(
        sessionToken: String,
        patientId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<Prescription>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsForPatient(
            sessionToken = sessionToken,
            patientId = patientId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }.mapList { it.toDomain() }

    override suspend fun getPrescriptionsByPrescriber(
        sessionToken: String,
        prescriberId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<Prescription>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsByPrescriber(
            sessionToken = sessionToken,
            prescriberId = prescriberId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }.mapList { it.toDomain() }

    override suspend fun getPrescriptionsForClinic(
        sessionToken: String,
        clinicId: String,
        status: String?,
        limit: Int?,
        offset: Int?,
    ): Result<List<Prescription>> = runCatchingNetworkExceptions {
        prescriptionApi.getPrescriptionsForClinic(
            sessionToken = sessionToken,
            clinicId = clinicId,
            status = status,
            limit = limit,
            offset = offset,
        )
    }.mapList { it.toDomain() }

    override suspend fun claimPrescription(
        sessionToken: String,
        userId: String,
        accessCode: String,
        birthday: String,
    ): Result<InitialSettings?> = runCatchingNetworkExceptions {
        prescriptionApi.claimPrescription(
            sessionToken = sessionToken,
            userId = userId,
            requestBody = ClaimPrescriptionDto(accessCode = accessCode, birthday = birthday),
        )
    }.map { it.toInitialSettings() }

    override suspend fun getLatestPrescription(
        sessionToken: String,
        userId: String,
    ): Result<ClaimedPrescription?> = runCatchingNetworkExceptions {
        prescriptionApi.getPatientPrescriptions(
            sessionToken = sessionToken,
            userId = userId,
        )
    }.map { list ->
        list.maxByOrNull { it.createdTime ?: "" }?.toClaimedPrescription()
    }
}