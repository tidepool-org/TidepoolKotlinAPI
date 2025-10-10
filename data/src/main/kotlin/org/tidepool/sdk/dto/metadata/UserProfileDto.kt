package org.tidepool.sdk.dto.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val fullName: String? = null,
    val patient: PatientDataDto? = null,
) {
    
    @Serializable
    data class PatientDataDto(
        val diagnosisType: DiagnosisTypeDto? = null,
        val diagnosisDate: String? = null,
        val birthday: String? = null,
        val biologicalSex: BiologicalSexDto? = null,
        val targetDevices: List<String>? = null,
        val targetTimezone: String? = null,
        val about: String? = null,
        val email: String? = null,
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
    val fullName: String? = null,
    val clinic: ClinicDataDto? = null,
) {
    
    @Serializable
    data class ClinicDataDto(
        val name: String? = null,
        val role: ClinicianRoleDto? = null,
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
    val fullName: String? = null,
    val patient: PatientDataDto? = null,
    val emails: List<String>? = null,
) {
    
    @Serializable
    data class PatientDataDto(
        val birthday: String? = null,
        val email: String? = null,
        val mrn: String? = null,
        val targetDevices: List<String>? = null,
        val targetTimezone: String? = null,
    )
}
