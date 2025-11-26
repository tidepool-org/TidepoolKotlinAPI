package org.tidepool.sdk.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class DatabaseConverters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }
    
    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? = instant?.toEpochMilliseconds()
}