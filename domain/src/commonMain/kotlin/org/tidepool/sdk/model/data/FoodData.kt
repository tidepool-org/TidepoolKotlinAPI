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
    override val annotations: List<Map<String, String>> = emptyList(),
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