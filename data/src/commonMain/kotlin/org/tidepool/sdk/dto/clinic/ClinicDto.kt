package org.tidepool.sdk.dto.clinic

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.BloodGlucoseDto
import org.tidepool.sdk.model.BloodGlucose
import org.tidepool.sdk.model.clinic.Clinic
import org.tidepool.sdk.model.clinic.ClinicSize
import org.tidepool.sdk.model.clinic.ClinicType
import java.time.Instant

@Serializable
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
    val clinicType: ClinicTypeDto? = null,
    @SerialName("clinicSize")
    val clinicSize: ClinicSizeDto? = null,
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
)

@Serializable
enum class ClinicTypeDto {

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
enum class ClinicSizeDto {

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

fun ClinicDto.toDomain(): Clinic = Clinic(
    id = id,
    name = name,
    shareCode = shareCode,
    address = address,
    city = city,
    postalCode = postalCode,
    state = state,
    country = country,
    patientTags = patientTags?.map { it.toDomain() },
    sites = sites?.map { it.toDomain() },
    lastDeletedPatientTag = lastDeletedPatientTag?.toDomain(),
    phoneNumbers = phoneNumbers?.toDomain(),
    clinicType = clinicType?.toDomain(),
    clinicSize = clinicSize?.toDomain(),
    canMigrate = canMigrate,
    website = website,
    createdTime = createdTime,
    updatedTime = updatedTime,
    tierDescription = tierDescription,
    tier = tier,
    preferredBgUnits = this.preferredBgUnits.toDomain(),
    suppressedNotifications = suppressedNotifications?.toDomain(),
    timezone = timezone
)

private fun BloodGlucoseDto.UnitsDto.toDomain(): BloodGlucose.Units = when (this) {
    BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter -> BloodGlucose.Units.MilligramsPerDeciliter
    BloodGlucoseDto.UnitsDto.MillimolesPerLiter -> BloodGlucose.Units.MillimolesPerLiter
}

private fun ClinicSizeDto.toDomain(): ClinicSize = when (this) {
    ClinicSizeDto.Small -> ClinicSize.Small
    ClinicSizeDto.Medium -> ClinicSize.Medium
    ClinicSizeDto.Large -> ClinicSize.Large
    ClinicSizeDto.Huge -> ClinicSize.Huge
}

private fun ClinicTypeDto.toDomain(): ClinicType = when (this) {
    ClinicTypeDto.ProviderPractice -> ClinicType.ProviderPractice
    ClinicTypeDto.HealthcareSystem -> ClinicType.HealthcareSystem
    ClinicTypeDto.VeterinaryClinic -> ClinicType.VeterinaryClinic
    ClinicTypeDto.Researcher -> ClinicType.Researcher
    ClinicTypeDto.Other -> ClinicType.Other
}

public fun Clinic.toDto(): ClinicDto = ClinicDto(
    id = id,
    name = name,
    shareCode = shareCode,
    address = address,
    city = city,
    postalCode = postalCode,
    state = state,
    country = country,
    patientTags = patientTags?.map { it.toDto() },
    sites = sites?.map { it.toDto() },
    lastDeletedPatientTag = lastDeletedPatientTag?.let { it.toDto() },
    phoneNumbers = phoneNumbers?.let { it.toDto() },
    clinicType= clinicType?.toDto(),
    clinicSize= clinicSize?.toDto(),
    canMigrate = canMigrate,
    website = website,
    createdTime = createdTime,
    updatedTime = updatedTime,
    tierDescription = tierDescription,
    tier = tier,
    preferredBgUnits = when (preferredBgUnits) {
        BloodGlucose.Units.MilligramsPerDeciliter -> BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter
        BloodGlucose.Units.MillimolesPerLiter -> BloodGlucoseDto.UnitsDto.MillimolesPerLiter
    },
    suppressedNotifications = suppressedNotifications?.let {
        it.toDto()
    },
    timezone = timezone
)

private fun BloodGlucose.Units.toDto(): BloodGlucoseDto.UnitsDto = when (this) {
    BloodGlucose.Units.MilligramsPerDeciliter -> BloodGlucoseDto.UnitsDto.MilligramsPerDeciliter
    BloodGlucose.Units.MillimolesPerLiter -> BloodGlucoseDto.UnitsDto.MillimolesPerLiter
}

private fun ClinicSize.toDto(): ClinicSizeDto = when (this) {
    ClinicSize.Small -> ClinicSizeDto.Small
    ClinicSize.Medium -> ClinicSizeDto.Medium
    ClinicSize.Large -> ClinicSizeDto.Large
    ClinicSize.Huge -> ClinicSizeDto.Huge
}

private fun ClinicType.toDto(): ClinicTypeDto = when (this) {
    ClinicType.ProviderPractice -> ClinicTypeDto.ProviderPractice
    ClinicType.HealthcareSystem -> ClinicTypeDto.HealthcareSystem
    ClinicType.VeterinaryClinic -> ClinicTypeDto.VeterinaryClinic
    ClinicType.Researcher -> ClinicTypeDto.Researcher
    ClinicType.Other -> ClinicTypeDto.Other
}
