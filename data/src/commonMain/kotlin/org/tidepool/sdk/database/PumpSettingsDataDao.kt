package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.PumpSettingsDataEntity

@Dao
interface PumpSettingsDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: PumpSettingsDataEntity)

    @Delete
    suspend fun delete(data: PumpSettingsDataEntity)

    @Delete
    suspend fun delete(data: List<PumpSettingsDataEntity>)

    @Query("DELETE FROM pump_settings_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM pump_settings_data WHERE id = :id")
    suspend fun getById(id: String): PumpSettingsDataEntity?

    @Query("SELECT * FROM pump_settings_data")
    suspend fun getAll(): List<PumpSettingsDataEntity>

    @Query("SELECT * FROM pump_settings_data ORDER BY time ASC, rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<PumpSettingsDataEntity>
}
