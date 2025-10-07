package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto
import java.time.Instant

@Serializable
data class ClinicDto(
    val id: String,
    val name: String,
    val shareCode: String,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    val country: String? = null,
    val patientTags: List<PatientTagDto>? = null,
    val sites: List<SiteDto>? = null,
    val lastDeletedPatientTag: PatientTagDto? = null,
    val phoneNumbers: PhoneNumbersDto? = null,
    val clinicType: ClinicType? = null,
    val clinicSize: ClinicSize? = null,
    val canMigrate: Boolean,
    val website: String? = null,
    @Contextual val createdTime: Instant,
    @Contextual val updatedTime: Instant,
    val tierDescription: String,
    val tier: String,
    val preferredBgUnits: BloodGlucoseDto.UnitsDto,
    val suppressedNotifications: SuppressedNotificationsDto? = null,
    val timezone: String? = null,
)

@Serializable
enum class ClinicType {
    
    @SerialName("provider_practice")
    ProviderPractice,
    
    @SerialName("healthcare_system")
    HealthcareSystem,
    
    @SerialName("veterinary_clinic")
    VeterinaryClinic,
    
    @SerialName("researcher")
    Researcher,
    
    @SerialName("other")
    Other,
}

@Serializable
enum class ClinicSize {
    
    @SerialName("0-249")
    Small,
    
    @SerialName("250-499")
    Medium,
    
    @SerialName("500-999")
    Large,
    
    @SerialName("1000+")
    Huge,
    ;
}