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
    FoodDataDto.MealDto.breakfast -> FoodData.Meal.Breakfast
    FoodDataDto.MealDto.lunch     -> FoodData.Meal.Lunch
    FoodDataDto.MealDto.dinner    -> FoodData.Meal.Dinner
    FoodDataDto.MealDto.snack     -> FoodData.Meal.Snack
    FoodDataDto.MealDto.other     -> FoodData.Meal.Other
}