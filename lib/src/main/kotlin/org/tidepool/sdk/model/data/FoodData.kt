package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.FoodDataDto

// schema food.v1
data class FoodData(
    val brand: String? = null,
    val code: String? = null,
    val meal: Meal? = null,
    val mealOther: String? = null,
    val name: String? = null,
) : BaseData(DataType.Food) {
    
    val amount: Nothing
        get() = TODO("schema \"amount.v1\" not implemented")
    val ingredients: Nothing
        get() = TODO("schema \"ingredientarray.v1\" not implemented")
    val nutrition: Nothing
        get() = TODO("schema \"nutrition.v1\" not implemented")
    
    enum class Meal {
        Breakfast,
        Lunch,
        Dinner,
        Snack,
        Other,
    }
}

internal fun FoodDataDto.toDomain(): FoodData = FoodData(
    brand = brand,
    code = code,
    meal = meal?.toDomain(),
    mealOther = mealOther,
    name = name,
)

internal fun FoodDataDto.MealDto.toDomain(): FoodData.Meal = when (this) {
    FoodDataDto.MealDto.Breakfast -> FoodData.Meal.Breakfast
    FoodDataDto.MealDto.Lunch  -> FoodData.Meal.Lunch
    FoodDataDto.MealDto.Dinner -> FoodData.Meal.Dinner
    FoodDataDto.MealDto.Snack -> FoodData.Meal.Snack
    FoodDataDto.MealDto.Other -> FoodData.Meal.Other
}

internal fun FoodData.toDto(): FoodDataDto = FoodDataDto(
    brand = brand,
    code = code,
    meal = meal?.toDto(),
    mealOther = mealOther,
    name = name,
)

internal fun FoodData.Meal.toDto(): FoodDataDto.MealDto = when (this) {
    FoodData.Meal.Breakfast -> FoodDataDto.MealDto.Breakfast
    FoodData.Meal.Lunch     -> FoodDataDto.MealDto.Lunch
    FoodData.Meal.Dinner    -> FoodDataDto.MealDto.Dinner
    FoodData.Meal.Snack     -> FoodDataDto.MealDto.Snack
    FoodData.Meal.Other     -> FoodDataDto.MealDto.Other
}