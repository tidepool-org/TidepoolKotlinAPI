package org.tidepool.sdk.model.clinic

import org.tidepool.sdk.model.BloodGlucose
import java.time.Instant

data class Clinic(
    val id: String,
    val name: String,
    val shareCode: String,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    val country: String? = null,
    val patientTags: List<PatientTag>? = null,
    val sites: List<Site>? = null,
    val lastDeletedPatientTag: PatientTag? = null,
    val phoneNumbers: PhoneNumbers? = null,
    val clinicType: ClinicType? = null,
    val clinicSize: ClinicSize? = null,
    val canMigrate: Boolean,
    val website: String? = null,
    val createdTime: Instant,
    val updatedTime: Instant,
    val tierDescription: String,
    val tier: String,
    val preferredBgUnits: BloodGlucose.Units,
    val suppressedNotifications: SuppressedNotifications? = null,
    val timezone: String? = null,
)

enum class ClinicType {
    ProviderPractice,
    HealthcareSystem,
    VeterinaryClinic,
    Researcher,
    Other,
}

enum class ClinicSize {
    Small,
    Medium,
    Large,
    Huge,
    ;
}