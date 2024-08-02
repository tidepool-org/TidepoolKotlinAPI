package org.tidepool.sdk.model.data

// schema insulin.v1
data class InsulinData(
	val dose: Dose,
	val site: String?
) : BaseData(DataType.insulin) {
	val formulation: Nothing
		get() = TODO("schema \"formulation.v1\" not implemented")
}