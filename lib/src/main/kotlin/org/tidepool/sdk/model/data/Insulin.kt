package org.tidepool.sdk.model.data

class Insulin {
	enum class Units {
		Units
	}
}

// schema dose.v1
data class Dose(
	val units: Insulin.Units,
	val total: Double,
	val food: Double?,
	val correction: Double?,
	val active: Double?,
)