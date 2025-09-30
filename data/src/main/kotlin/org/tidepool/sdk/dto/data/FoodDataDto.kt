package org.tidepool.sdk.dto.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// schema food.v1
@Serializable
data class FoodDataDto(
    val brand: String? = null,
    val code: String? = null,
    val meal: MealDto? = null,
    val mealOther: String? = null,
    val name: String? = null,
) : BaseDataDto(DataTypeDto.Food) {
    
    val amount: Nothing
        get() = TODO("schema \"amount.v1\" not implemented")
    val ingredients: Nothing
        get() = TODO("schema \"ingredientarray.v1\" not implemented")
    val nutrition: Nothing
        get() = TODO("schema \"nutrition.v1\" not implemented")
    
    @Serializable
    enum class MealDto {
        
        @SerialName("breakfast")
        breakfast,
        
        @SerialName("lunch")
        lunch,
        
        @SerialName("dinner")
        dinner,
        
        @SerialName("snack")
        snack,
        
        @SerialName("other")
        other,
    }
}