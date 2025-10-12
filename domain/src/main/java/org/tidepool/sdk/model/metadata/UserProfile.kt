package org.tidepool.sdk.model.metadata

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