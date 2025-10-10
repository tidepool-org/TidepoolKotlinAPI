package org.tidepool.sdk.model.metadata

import org.tidepool.sdk.dto.metadata.UserProfileDto
import org.tidepool.sdk.dto.metadata.ClinicianProfileDto
import org.tidepool.sdk.dto.metadata.PatientProfileDto

data class UserProfile(
    val fullName: String? = null,
    val patient: PatientData? = null,
) {
    
    data class PatientData(
        val diagnosisType: DiagnosisType? = null,
        val diagnosisDate: String? = null,
        val birthday: String? = null,
        val biologicalSex: BiologicalSex? = null,
        val targetDevices: List<String>? = null,
        val targetTimezone: String? = null,
        val about: String? = null,
        val email: String? = null,
        val mrn: String? = null,
    )
    
    enum class DiagnosisType {
        Type1,
        Type2,
        Gestational,
        Prediabetes,
        Lada,
        Mody,
        Other,
        ;
    }
    
    enum class BiologicalSex {
        Male,
        Female,
        ;
    }
}

data class ClinicianProfile(
    val fullName: String? = null,
    val clinic: ClinicData? = null,
) {
    
    data class ClinicData(
        val name: String? = null,
        val role: ClinicianRole? = null,
        val telephone: String? = null,
    )
    
    enum class ClinicianRole {
        ClinicManager,
        DiabetesEducator,
        Endocrinologist,
        FrontDesk,
        InformationTechnology,
        MedicalAssistant,
        Nurse,
        PrimaryCarePhysician,
        PhysicianAssistant,
        Other
    }
}

data class PatientProfile(
    val fullName: String? = null,
    val patient: PatientData? = null,
    val emails: List<String>? = null,
) {
    
    data class PatientData(
        val birthday: String? = null,
        val email: String? = null,
        val mrn: String? = null,
        val targetDevices: List<String>? = null,
        val targetTimezone: String? = null,
    )
}

// UserProfile mappers
internal fun UserProfile.toDto() = UserProfileDto(
    fullName = fullName,
    patient = patient?.toDto(),
)

internal fun UserProfileDto.toDomain() = UserProfile(
    fullName = fullName,
    patient = patient?.toDomain(),
)

internal fun UserProfile.PatientData.toDto() = UserProfileDto.PatientDataDto(
    diagnosisType = diagnosisType?.toDto(),
    diagnosisDate = diagnosisDate,
    birthday = birthday,
    biologicalSex = biologicalSex?.toDto(),
    targetDevices = targetDevices,
    targetTimezone = targetTimezone,
    about = about,
    email = email,
    mrn = mrn,
)

internal fun UserProfileDto.PatientDataDto.toDomain() = UserProfile.PatientData(
    diagnosisType = diagnosisType?.toDomain(),
    diagnosisDate = diagnosisDate,
    birthday = birthday,
    biologicalSex = biologicalSex?.toDomain(),
    targetDevices = targetDevices,
    targetTimezone = targetTimezone,
    about = about,
    email = email,
    mrn = mrn,
)

internal fun UserProfile.DiagnosisType.toDto() = when (this) {
    UserProfile.DiagnosisType.Type1       -> UserProfileDto.DiagnosisTypeDto.Type1
    UserProfile.DiagnosisType.Type2       -> UserProfileDto.DiagnosisTypeDto.Type2
    UserProfile.DiagnosisType.Gestational -> UserProfileDto.DiagnosisTypeDto.Gestational
    UserProfile.DiagnosisType.Prediabetes -> UserProfileDto.DiagnosisTypeDto.Prediabetes
    UserProfile.DiagnosisType.Lada  -> UserProfileDto.DiagnosisTypeDto.Lada
    UserProfile.DiagnosisType.Mody  -> UserProfileDto.DiagnosisTypeDto.Mody
    UserProfile.DiagnosisType.Other -> UserProfileDto.DiagnosisTypeDto.Other
}

internal fun UserProfileDto.DiagnosisTypeDto.toDomain() = when (this) {
    UserProfileDto.DiagnosisTypeDto.Type1 -> UserProfile.DiagnosisType.Type1
    UserProfileDto.DiagnosisTypeDto.Type2 -> UserProfile.DiagnosisType.Type2
    UserProfileDto.DiagnosisTypeDto.Gestational -> UserProfile.DiagnosisType.Gestational
    UserProfileDto.DiagnosisTypeDto.Prediabetes -> UserProfile.DiagnosisType.Prediabetes
    UserProfileDto.DiagnosisTypeDto.Lada        -> UserProfile.DiagnosisType.Lada
    UserProfileDto.DiagnosisTypeDto.Mody        -> UserProfile.DiagnosisType.Mody
    UserProfileDto.DiagnosisTypeDto.Other -> UserProfile.DiagnosisType.Other
}

internal fun UserProfile.BiologicalSex.toDto() = when (this) {
    UserProfile.BiologicalSex.Male   -> UserProfileDto.BiologicalSexDto.Male
    UserProfile.BiologicalSex.Female -> UserProfileDto.BiologicalSexDto.Female
}

internal fun UserProfileDto.BiologicalSexDto.toDomain() = when (this) {
    UserProfileDto.BiologicalSexDto.Male   -> UserProfile.BiologicalSex.Male
    UserProfileDto.BiologicalSexDto.Female -> UserProfile.BiologicalSex.Female
}

// ClinicianProfile mappers
internal fun ClinicianProfile.toDto() = ClinicianProfileDto(
    fullName = fullName,
    clinic = clinic?.toDto(),
)

internal fun ClinicianProfileDto.toDomain() = ClinicianProfile(
    fullName = fullName,
    clinic = clinic?.toDomain(),
)

internal fun ClinicianProfile.ClinicData.toDto() = ClinicianProfileDto.ClinicDataDto(
    name = name,
    role = role?.toDto(),
    telephone = telephone,
)

internal fun ClinicianProfileDto.ClinicDataDto.toDomain() = ClinicianProfile.ClinicData(
    name = name,
    role = role?.toDomain(),
    telephone = telephone,
)

internal fun ClinicianProfile.ClinicianRole.toDto() = when (this) {
    ClinicianProfile.ClinicianRole.ClinicManager    -> ClinicianProfileDto.ClinicianRoleDto.ClinicManager
    ClinicianProfile.ClinicianRole.DiabetesEducator -> ClinicianProfileDto.ClinicianRoleDto.DiabetesEducator
    ClinicianProfile.ClinicianRole.Endocrinologist        -> ClinicianProfileDto.ClinicianRoleDto.Endocrinologist
    ClinicianProfile.ClinicianRole.FrontDesk             -> ClinicianProfileDto.ClinicianRoleDto.FrontDesk
    ClinicianProfile.ClinicianRole.InformationTechnology -> ClinicianProfileDto.ClinicianRoleDto.InformationTechnology
    ClinicianProfile.ClinicianRole.MedicalAssistant       -> ClinicianProfileDto.ClinicianRoleDto.MedicalAssistant
    ClinicianProfile.ClinicianRole.Nurse                -> ClinicianProfileDto.ClinicianRoleDto.Nurse
    ClinicianProfile.ClinicianRole.PrimaryCarePhysician -> ClinicianProfileDto.ClinicianRoleDto.PrimaryCarePhysician
    ClinicianProfile.ClinicianRole.PhysicianAssistant -> ClinicianProfileDto.ClinicianRoleDto.PhysicianAssistant
    ClinicianProfile.ClinicianRole.Other              -> ClinicianProfileDto.ClinicianRoleDto.Other
}

internal fun ClinicianProfileDto.ClinicianRoleDto.toDomain() = when (this) {
    ClinicianProfileDto.ClinicianRoleDto.ClinicManager    -> ClinicianProfile.ClinicianRole.ClinicManager
    ClinicianProfileDto.ClinicianRoleDto.DiabetesEducator -> ClinicianProfile.ClinicianRole.DiabetesEducator
    ClinicianProfileDto.ClinicianRoleDto.Endocrinologist  -> ClinicianProfile.ClinicianRole.Endocrinologist
    ClinicianProfileDto.ClinicianRoleDto.FrontDesk         -> ClinicianProfile.ClinicianRole.FrontDesk
    ClinicianProfileDto.ClinicianRoleDto.InformationTechnology -> ClinicianProfile.ClinicianRole.InformationTechnology
    ClinicianProfileDto.ClinicianRoleDto.MedicalAssistant      -> ClinicianProfile.ClinicianRole.MedicalAssistant
    ClinicianProfileDto.ClinicianRoleDto.Nurse                  -> ClinicianProfile.ClinicianRole.Nurse
    ClinicianProfileDto.ClinicianRoleDto.PrimaryCarePhysician -> ClinicianProfile.ClinicianRole.PrimaryCarePhysician
    ClinicianProfileDto.ClinicianRoleDto.PhysicianAssistant   -> ClinicianProfile.ClinicianRole.PhysicianAssistant
    ClinicianProfileDto.ClinicianRoleDto.Other                  -> ClinicianProfile.ClinicianRole.Other
}

// PatientProfile mappers
internal fun PatientProfile.toDto() = PatientProfileDto(
    fullName = fullName,
    patient = patient?.toDto(),
    emails = emails,
)

internal fun PatientProfileDto.toDomain() = PatientProfile(
    fullName = fullName,
    patient = patient?.toDomain(),
    emails = emails,
)

internal fun PatientProfile.PatientData.toDto() = PatientProfileDto.PatientDataDto(
    birthday = birthday,
    email = email,
    mrn = mrn,
    targetDevices = targetDevices,
    targetTimezone = targetTimezone,
)

internal fun PatientProfileDto.PatientDataDto.toDomain() = PatientProfile.PatientData(
    birthday = birthday,
    email = email,
    mrn = mrn,
    targetDevices = targetDevices,
    targetTimezone = targetTimezone,
)