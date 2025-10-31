package org.tidepool.sdk.dto.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.dto.toDomain
import org.tidepool.sdk.dto.toDto
import org.tidepool.sdk.model.data.FoodData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema food.v1
@Serializable
data class FoodDataDto(
    override val id: String = "",
    override val type: DataTypeDto = DataTypeDto.Alert,
    @Contextual
    override val time: Instant? = null,
    override val annotations: List<Map<String, String>> = emptyList(),
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
    override val timeZoneOffset: Duration? = null,
    
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
) : BaseDataDto() {
    
    val amount: Nothing
        get() = TODO("schema \"amount.v1\" not implemented")
    val ingredients: Nothing
        get() = TODO("schema \"ingredientarray.v1\" not implemented")
    val nutrition: Nothing
        get() = TODO("schema \"nutrition.v1\" not implemented")
    
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
)

fun FoodData.toDto(): FoodDataDto = FoodDataDto(
    id = id,
    type = type.toDto(),
    time = time,
    annotations = annotations,
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