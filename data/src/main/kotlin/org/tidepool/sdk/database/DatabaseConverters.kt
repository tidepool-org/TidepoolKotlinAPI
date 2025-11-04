package org.tidepool.sdk.database

import androidx.room.TypeConverter
import java.time.Instant

class DatabaseConverters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }
    
    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? = instant?.toEpochMilli()
}