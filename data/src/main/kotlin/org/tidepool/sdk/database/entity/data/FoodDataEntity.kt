package org.tidepool.sdk.database.entity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.tidepool.sdk.dto.data.FoodDataDto
import java.time.Instant

@Entity(tableName = "food_data")
data class FoodDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: String,
    @ColumnInfo(name = "type")
    override var type: String,
    @ColumnInfo(name = "time")
    override var time: Instant? = null,
    @ColumnInfo(name = "annotations")
    override var annotations: String? = null, // JSON string
    @ColumnInfo(name = "associations")
    override var associations: String? = null, // JSON string
    @ColumnInfo(name = "clock_drift_offset")
    override var clockDriftOffset: Long? = null, // Duration in milliseconds
    @ColumnInfo(name = "conversion_offset")
    override var conversionOffset: Long? = null, // Duration in milliseconds
    @ColumnInfo(name = "data_set_id")
    override var dataSetId: String? = null,
    @ColumnInfo(name = "device_time")
    override var deviceTime: String? = null,
    @ColumnInfo(name = "notes")
    override var notes: String? = null, // JSON string
    @ColumnInfo(name = "time_zone")
    override var timeZone: String? = null, // TimeZone ID
    @ColumnInfo(name = "time_zone_offset")
    override var timeZoneOffset: Long? = null, // Duration in milliseconds
    
    @ColumnInfo(name = "brand")
    var brand: String? = null,
    @ColumnInfo(name = "code")
    var code: String? = null,
    @ColumnInfo(name = "meal")
    var meal: String? = null,
    @ColumnInfo(name = "meal_other")
    var mealOther: String? = null,
    @ColumnInfo(name = "name")
    var name: String? = null
) : BaseDataEntity(
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
)

fun FoodDataDto.toEntity() = FoodDataEntity(
    id = id,
    type = Json.encodeToString(type),
    time = time,
    annotations = annotations.let { Json.encodeToString(it) },
    associations = associations.let { Json.encodeToString(it) },
    clockDriftOffset = clockDriftOffset?.inWholeMilliseconds,
    conversionOffset = conversionOffset?.inWholeMilliseconds,
    dataSetId = dataSetId,
    deviceTime = deviceTime,
    notes = notes.let { Json.encodeToString(it) },
    timeZone = timeZone?.id,
    timeZoneOffset = timeZoneOffset?.inWholeMilliseconds,
    brand = brand,
    code = code,
    meal = meal?.let { meal ->
        when (meal) {
            FoodDataDto.MealDto.Breakfast -> "breakfast"
            FoodDataDto.MealDto.Lunch     -> "lunch"
            FoodDataDto.MealDto.Dinner    -> "dinner"
            FoodDataDto.MealDto.Snack     -> "snack"
            FoodDataDto.MealDto.Other     -> "other"
        }
    },
    mealOther = mealOther,
    name = name
)
