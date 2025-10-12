package org.tidepool.sdk.dto.clinic

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.model.clinic.Clinic
import java.time.Instant

@Serializable
@KonvertTo(Clinic::class, mapFunctionName = "toDomain")
data class ClinicDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("shareCode")
    val shareCode: String,
    @SerialName("address")
    val address: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("postalCode")
    val postalCode: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("patientTags")
    val patientTags: List<PatientTagDto>? = null,
    @SerialName("sites")
    val sites: List<SiteDto>? = null,
    @SerialName("lastDeletedPatientTag")
    val lastDeletedPatientTag: PatientTagDto? = null,
    @SerialName("phoneNumbers")
    val phoneNumbers: PhoneNumbersDto? = null,
    @SerialName("clinicType")
    val clinicType: ClinicType? = null,
    @SerialName("clinicSize")
    val clinicSize: ClinicSize? = null,
    @SerialName("canMigrate")
    val canMigrate: Boolean,
    @SerialName("website")
    val website: String? = null,
    @Contextual
    @SerialName("createdTime")
    val createdTime: Instant,
    @Contextual
    @SerialName("updatedTime")
    val updatedTime: Instant,
    @SerialName("tierDescription")
    val tierDescription: String,
    @SerialName("tier")
    val tier: String,
    @SerialName("preferredBgUnits")
    val preferredBgUnits: BloodGlucoseDto.UnitsDto,
    @SerialName("suppressedNotifications")
    val suppressedNotifications: SuppressedNotificationsDto? = null,
    @SerialName("timezone")
    val timezone: String? = null,
) {
    @KonvertFrom(Clinic::class, mapFunctionName = "fromDomain")
    companion object
}

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