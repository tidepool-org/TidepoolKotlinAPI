package org.tidepool.sdk.dto.data

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tidepool.sdk.dto.AssociationDto
import org.tidepool.sdk.model.data.BasalAutomatedData
import org.tidepool.sdk.model.data.FoodData
import java.time.Instant
import java.util.TimeZone
import kotlin.time.Duration

// schema food.v1
@Serializable
@KonvertTo(FoodData::class, mapFunctionName = "toDomain")
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