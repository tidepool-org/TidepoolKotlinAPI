package org.tidepool.sdk.model.prescription

/**
 * A patient-facing prescription as returned by `GET /v1/patients/{userId}/prescriptions` and the
 * claim endpoint. Carries the therapy [initialSettings] plus the metadata the onboarding flow needs
 * to render the Therapy Settings overview ("Submitted by {prescriber}, {date}").
 */
data class ClaimedPrescription(
    val initialSettings: InitialSettings?,
    val prescriberUserId: String?,
    val submittedDate: String?,
    val state: String?,
)
