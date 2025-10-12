package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.FoodData

// schema food.v1
@Serializable
@KonvertTo(FoodData::class, mapFunctionName = "toDomain")
data class FoodDataDto(
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
) : BaseDataDto(DataTypeDto.Food) {
    
    @KonvertFrom(FoodData::class, mapFunctionName = "fromDomain")
    companion object {}
    
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