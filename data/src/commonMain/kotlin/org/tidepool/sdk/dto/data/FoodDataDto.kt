package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.FoodData
import kotlinx.datetime.Instant
import org.tidepool.sdk.model.data.EnergyUnit
import org.tidepool.sdk.model.data.NutrientUnit
import java.util.TimeZone
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

// schema food.v1
@Serializable
data class FoodDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Food,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>>? = null,
    override val associations: List<AssociationDto> = emptyList(),
    @Contextual
    override val clockDriftOffset: Duration? = null,
    @Contextual
    override val conversionOffset: Duration? = null,
    override val dataSetId: String? = null,
    override val deviceTime: String? = null,
    override val notes: List<String> = emptyList(),
    @Contextual
    override val timeZone: TimeZone? = null,
    @Contextual
    override val timeZoneOffset: Int? = null,
    
    @SerialName("brand")
    val brand: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("meal")
    val meal: MealDto? = null,
    @SerialName("mealOther")
    val mealOther: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("amount")
    val amount: AmountDto? = null,
    @SerialName("nutrition")
    val nutrition: NutritionDto? = null,
    @SerialName("ingredients")
    val ingredients: List<IngredientDto>? = null,
) : BaseDataDto() {
    
    @Serializable
    data class AmountDto(
        @SerialName("value")
        val value: Double? = null,
        @SerialName("units")
        val units: String? = null,
    )
    
    @Serializable
    data class NutritionDto(
        @SerialName("carbohydrate")
        val carbohydrate: CarbohydrateDto? = null,
        @SerialName("fat")
        val fat: NutrientDto? = null,
        @SerialName("protein")
        val protein: NutrientDto? = null,
        @SerialName("energy")
        val energy: EnergyDto? = null,
        @SerialName("estimatedAbsorptionDuration")
        val estimatedAbsorptionDuration: Int? = null, // Duration in seconds
    )
    
    @Serializable
    data class CarbohydrateDto(
        @SerialName("net")
        val net: Double? = null,
        @SerialName("sugars")
        val sugars: Double? = null,
        @SerialName("dietaryFiber")
        val dietaryFiber: Double? = null,
        @SerialName("total")
        val total: Double? = null,
        @SerialName("units")
        val units: NutrientUnitDto? = NutrientUnitDto.Grams,
    )
    
    @Serializable
    data class NutrientDto(
        @SerialName("total")
        val total: Double? = null,
        @SerialName("units")
        val units: NutrientUnitDto? = NutrientUnitDto.Grams,
    )
    
    @Serializable
    data class EnergyDto(
        @SerialName("value")
        val value: Double? = null,
        @SerialName("units")
        val units: EnergyUnitsDto? = null,
    )
    
    @Serializable
    data class IngredientDto(
        @SerialName("name")
        val name: String? = null,
        @SerialName("amount")
        val amount: AmountDto? = null,
        @SerialName("brand")
        val brand: String? = null,
        @SerialName("code")
        val code: String? = null,
        @SerialName("nutrition")
        val nutrition: NutritionDto? = null,
        @SerialName("ingredients")
        val ingredients: List<IngredientDto>? = null,
    )
    
    @Serializable
    enum class MealDto {
        
        @SerialName("breakfast")
        Breakfast,
        
        @SerialName("lunch")
        Lunch,
        
        @SerialName("dinner")
        Dinner,
        
        @SerialName("snack")
        Snack,
        
        @SerialName("other")
        Other,
    }
}

fun FoodDataDto.toDomain(): FoodData = FoodData(
    id = id,
    type = type.toDomain(),
    time = time,
    annotations = annotations,
    associations = associations.map { it.toDomain() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    brand = brand,
    code = code,
    meal = meal?.toDomain(),
    mealOther = mealOther,
    name = name,
    amount = amount?.toDomain(),
    nutrition = nutrition?.toDomain(),
    ingredients = ingredients?.map { it.toDomain() },
)

fun FoodData.toDto(): FoodDataDto = FoodDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations?.takeUnless { it.isEmpty() },
    associations = associations.map { it.toDto() },
    clockDriftOffset = clockDriftOffset,
    conversionOffset = conversionOffset,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes,
    timeZone = timeZone,
    timeZoneOffset = timeZoneOffset,
    brand = brand,
    code = code,
    meal = meal?.toDto(),
    mealOther = mealOther,
    name = name,
    amount = amount?.toDto(),
    nutrition = nutrition?.toDto(),
    ingredients = ingredients?.map { it.toDto() },
)

private fun FoodDataDto.MealDto.toDomain(): FoodData.Meal = when (this) {
    FoodDataDto.MealDto.Breakfast -> FoodData.Meal.Breakfast
    FoodDataDto.MealDto.Lunch -> FoodData.Meal.Lunch
    FoodDataDto.MealDto.Dinner -> FoodData.Meal.Dinner
    FoodDataDto.MealDto.Snack -> FoodData.Meal.Snack
    FoodDataDto.MealDto.Other -> FoodData.Meal.Other
}

private fun FoodData.Meal.toDto(): FoodDataDto.MealDto = when (this) {
    FoodData.Meal.Breakfast -> FoodDataDto.MealDto.Breakfast
    FoodData.Meal.Lunch -> FoodDataDto.MealDto.Lunch
    FoodData.Meal.Dinner -> FoodDataDto.MealDto.Dinner
    FoodData.Meal.Snack -> FoodDataDto.MealDto.Snack
    FoodData.Meal.Other -> FoodDataDto.MealDto.Other
}

private fun FoodDataDto.AmountDto.toDomain(): FoodData.Amount = FoodData.Amount(
    value = value,
    units = units,
)

private fun FoodData.Amount.toDto(): FoodDataDto.AmountDto = FoodDataDto.AmountDto(
    value = value,
    units = units,
)

private fun FoodDataDto.NutritionDto.toDomain(): FoodData.Nutrition = FoodData.Nutrition(
    carbohydrate = carbohydrate?.toDomain(),
    fat = fat?.toDomain(),
    protein = protein?.toDomain(),
    energy = energy?.toDomain(),
    estimatedAbsorptionDuration = estimatedAbsorptionDuration?.seconds,
)

private fun FoodData.Nutrition.toDto(): FoodDataDto.NutritionDto = FoodDataDto.NutritionDto(
    carbohydrate = carbohydrate?.toDto(),
    fat = fat?.toDto(),
    protein = protein?.toDto(),
    energy = energy?.toDto(),
    estimatedAbsorptionDuration = estimatedAbsorptionDuration?.inWholeSeconds?.toInt(),
)

private fun FoodDataDto.CarbohydrateDto.toDomain(): FoodData.Carbohydrate = FoodData.Carbohydrate(
    net = net,
    sugars = sugars,
    dietaryFiber = dietaryFiber,
    total = total,
    units = units?.toDomain(),
)

private fun FoodData.Carbohydrate.toDto(): FoodDataDto.CarbohydrateDto = FoodDataDto.CarbohydrateDto(
    net = net,
    sugars = sugars,
    dietaryFiber = dietaryFiber,
    total = total,
    units = units?.toDto(),
)

private fun NutrientUnit.toDto(): NutrientUnitDto = when (this) {
    NutrientUnit.Grams -> NutrientUnitDto.Grams
}

private fun NutrientUnitDto.toDomain(): NutrientUnit = when (this) {
    NutrientUnitDto.Grams -> NutrientUnit.Grams
}

private fun FoodDataDto.NutrientDto.toDomain(): FoodData.Nutrient = FoodData.Nutrient(
    total = total,
    units = units?.toDomain(),
)

private fun FoodData.Nutrient.toDto(): FoodDataDto.NutrientDto = FoodDataDto.NutrientDto(
    total = total,
    units = units?.toDto(),
)

private fun FoodDataDto.EnergyDto.toDomain(): FoodData.Energy = FoodData.Energy(
    value = value,
    units = units?.toDomain(),
)

private fun FoodData.Energy.toDto(): FoodDataDto.EnergyDto = FoodDataDto.EnergyDto(
    value = value,
    units = units?.toDto(),
)

private fun EnergyUnitsDto.toDomain(): EnergyUnit = when (this) {
    EnergyUnitsDto.Calories -> EnergyUnit.Calories
    EnergyUnitsDto.Joules -> EnergyUnit.Joules
    EnergyUnitsDto.Kilocalories -> EnergyUnit.Kilocalories
    EnergyUnitsDto.Kilojoules -> EnergyUnit.Kilojoules
}

private fun EnergyUnit.toDto(): EnergyUnitsDto = when (this) {
    EnergyUnit.Calories -> EnergyUnitsDto.Calories
    EnergyUnit.Joules -> EnergyUnitsDto.Joules
    EnergyUnit.Kilocalories -> EnergyUnitsDto.Kilocalories
    EnergyUnit.Kilojoules -> EnergyUnitsDto.Kilojoules
}

private fun FoodDataDto.IngredientDto.toDomain(): FoodData.Ingredient = FoodData.Ingredient(
    name = name,
    amount = amount?.toDomain(),
    brand = brand,
    code = code,
    nutrition = nutrition?.toDomain(),
    ingredients = ingredients?.map { it.toDomain() },
)

private fun FoodData.Ingredient.toDto(): FoodDataDto.IngredientDto = FoodDataDto.IngredientDto(
    name = name,
    amount = amount?.toDto(),
    brand = brand,
    code = code,
    nutrition = nutrition?.toDto(),
    ingredients = ingredients?.map { it.toDto() },
)

