package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.metadata.ClinicianProfile
import org.tidepool.sdk.model.metadata.PatientProfile
import org.tidepool.sdk.model.metadata.UserProfile

@Serializable
data class UserProfileDto(
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("patient")
    val patient: PatientDataDto? = null,
)  {

    @Serializable
    data class PatientDataDto(
        @SerialName("diagnosisType")
        val diagnosisType: DiagnosisTypeDto? = null,
        @SerialName("diagnosisDate")
        val diagnosisDate: String? = null,
        @SerialName("birthday")
        val birthday: String? = null,
        @SerialName("biologicalSex")
        val biologicalSex: BiologicalSexDto? = null,
        @SerialName("targetDevices")
        val targetDevices: List<String>? = null,
        @SerialName("targetTimezone")
        val targetTimezone: String? = null,
        @SerialName("about")
        val about: String? = null,
        @SerialName("email")
        val email: String? = null,
        @SerialName("mrn")
        val mrn: String? = null,
    )
    @Serializable
    enum class DiagnosisTypeDto {
        
        @SerialName("type1")
        Type1,
        
        @SerialName("type2")
        Type2,
        
        @SerialName("gestational")
        Gestational,
        
        @SerialName("prediabetes")
        Prediabetes,
        
        @SerialName("lada")
        Lada,
        
        @SerialName("mody")
        Mody,
        
        @SerialName("other")
        Other,
        ;
    }
    
    @Serializable
    enum class BiologicalSexDto {
        
        @SerialName("male")
        Male,
        
        @SerialName("female")
        Female,
        ;
    }
}

@Serializable
data class ClinicianProfileDto(
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("clinic")
    val clinic: ClinicDataDto? = null,
) {
    
    @Serializable
    data class ClinicDataDto(
        @SerialName("name")
        val name: String? = null,
        @SerialName("role")
        val role: ClinicianRoleDto? = null,
        @SerialName("telephone")
        val telephone: String? = null,
    )
    
    @Serializable
    enum class ClinicianRoleDto {
        @SerialName("clinic_manager")
        ClinicManager,
        @SerialName("diabetes_educator")
        DiabetesEducator,
        @SerialName("endocrinologist")
        Endocrinologist,
        @SerialName("front_desk")
        FrontDesk,
        @SerialName("information_technology")
        InformationTechnology,
        @SerialName("medical_assistant")
        MedicalAssistant,
        @SerialName("nurse")
        Nurse,
        @SerialName("primary_care_physician")
        PrimaryCarePhysician,
        @SerialName("physician_assistant")
        PhysicianAssistant,
        @SerialName("other")
        Other,
        ;
    }
}

@Serializable
data class PatientProfileDto(
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("patient")
    val patient: PatientDataDto? = null,
    @SerialName("emails")
    val emails: List<String>? = null,
) {
    
    @Serializable
    data class PatientDataDto(
        @SerialName("birthday")
        val birthday: String? = null,
        @SerialName("email")
        val email: String? = null,
        @SerialName("mrn")
        val mrn: String? = null,
        @SerialName("targetDevices")
        val targetDevices: List<String>? = null,
        @SerialName("targetTimezone")
        val targetTimezone: String? = null,
    )
}

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

fun ClinicianProfileDto.ClinicDataDto.toDomain() = ClinicianProfile.ClinicData(
    name = name,
    role = role?.toDomain(),
    telephone = telephone,
)

fun ClinicianProfileDto.toDomain(): ClinicianProfile = ClinicianProfile(
    fullName = fullName,
    clinic = clinic?.toDomain(),
)

fun UserProfileDto.PatientDataDto.toDomain(): UserProfile.PatientData = UserProfile.PatientData(
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

fun PatientProfileDto.PatientDataDto.toDomain(): PatientProfile.PatientData = PatientProfile.PatientData(
    birthday = birthday,
    email = email,
    mrn = mrn,
    targetDevices = targetDevices,
    targetTimezone = targetTimezone,
)

fun UserProfile.PatientData.toDto(): UserProfileDto.PatientDataDto = UserProfileDto.PatientDataDto(
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

fun PatientProfileDto.toDomain(): PatientProfile = PatientProfile(
    fullName = fullName,
    patient = patient?.toDomain(),
    emails = emails
)

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    fullName = fullName,
    patient = patient?.toDomain(),
)

fun UserProfile.toDto(): UserProfileDto = UserProfileDto(
    fullName = fullName,
    patient = patient?.toDto(),
)