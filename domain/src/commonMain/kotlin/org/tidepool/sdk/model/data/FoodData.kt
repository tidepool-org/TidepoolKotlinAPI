package org.tidepool.sdk.model.data

import org.tidepool.sdk.model.Association
import kotlinx.datetime.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema food.v1
data class FoodData(
    override val id: String,
    override val type: DataType = DataType.Food,
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<Association> = emptyList(),
    override val clockDriftOffset: Duration? = null,
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    override val timeZone: TimeZone? = null,
    override val timeZoneOffset: Int? = null,
    val brand: String? = null,
    val code: String? = null,
    val meal: Meal? = null,
    val mealOther: String? = null,
    val name: String? = null,
    val amount: Amount? = null,
    val nutrition: Nutrition? = null,
    val ingredients: List<Ingredient>? = null,
) : BaseData(
    id = id,
    type = type,
    time = time,
    annotations = annotations,
    associations = associations,
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
) {
    
    enum class Meal {
        Breakfast,
        Lunch,
        Dinner,
        Snack,
        Other,
    }
    
    data class Amount(
        val value: Double? = null,
        val units: String? = null,
    )
    
    data class Nutrition(
        val carbohydrate: Carbohydrate? = null,
        val fat: Nutrient? = null,
        val protein: Nutrient? = null,
        val energy: Energy? = null,
        val estimatedAbsorptionDuration: Duration? = null,
    )
    
    data class Carbohydrate(
        val net: Double? = null,
        val sugars: Double? = null,
        val dietaryFiber: Double? = null,
        val total: Double? = null,
        val units: NutrientUnit? = NutrientUnit.Grams,
    )
    
    data class Nutrient(
        val total: Double? = null,
        val units: NutrientUnit? = NutrientUnit.Grams,
    )

    data class Energy(
        val value: Double? = null,
        val units: EnergyUnit? = null,
    )

    data class Ingredient(
        val name: String? = null,
        val amount: Amount? = null,
        val brand: String? = null,
        val code: String? = null,
        val nutrition: Nutrition? = null,
        val ingredients: List<Ingredient>? = null,
    )
}
