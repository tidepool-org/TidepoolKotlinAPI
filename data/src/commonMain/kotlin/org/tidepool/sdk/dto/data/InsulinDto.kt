package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import org.tidepool.sdk.model.data.Dose
import org.tidepool.sdk.model.data.Insulin

@Serializable
class InsulinDto {

    /**
     * The one insulin-units DTO enum, shared by the insulin dose, pump status reservoir, pump
     * settings insulin units/amountMaximum and dosing decision units.insulin. Platform only
     * accepts "Units" (data/types/insulin/dose.go, data/types/status/pump/reservoir.go);
     * [JsonNames] keeps decoding old cached rows that were queued under the previous lowercase
     * "units" spelling.
     */
    @Serializable
    enum class UnitsDto {

        @SerialName("Units")
        @JsonNames("units")
        Units
    }
}

// schema dose.v1
@Serializable
data class DoseDto(
    @SerialName("units")
    val units: InsulinDto.UnitsDto,
    @SerialName("total")
    val total: Double,
    @SerialName("food")
    val food: Double?,
    @SerialName("correction")
    val correction: Double?,
    @SerialName("active")
    val active: Double?,
)

fun DoseDto.toDomain(): Dose = Dose(
    units = when (units) {
        InsulinDto.UnitsDto.Units -> Insulin.Units.Units
    },
    total = total,
    food = food,
    correction = correction,
    active = active
)

fun Dose.toDto(): DoseDto = DoseDto(
    units = when (units) {
        Insulin.Units.Units -> InsulinDto.UnitsDto.Units
    },
    total = total,
    food = food,
    correction = correction,
    active = active
)