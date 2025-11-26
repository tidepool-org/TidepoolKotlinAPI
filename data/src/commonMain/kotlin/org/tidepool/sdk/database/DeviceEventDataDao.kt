package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.DeviceEventDataEntity

@Dao
interface DeviceEventDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DeviceEventDataEntity)

    @Delete
    suspend fun delete(data: DeviceEventDataEntity)

    @Query("SELECT * FROM device_event_data WHERE id = :id")
    suspend fun getById(id: String): DeviceEventDataEntity?

    @Query("SELECT * FROM device_event_data")
    suspend fun getAll(): List<DeviceEventDataEntity>
}
