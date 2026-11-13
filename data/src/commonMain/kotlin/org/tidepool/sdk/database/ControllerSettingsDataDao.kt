package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.ControllerSettingsDataEntity

@Dao
interface ControllerSettingsDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ControllerSettingsDataEntity)

    @Delete
    suspend fun delete(data: ControllerSettingsDataEntity)

    @Delete
    suspend fun delete(data: List<ControllerSettingsDataEntity>)

    @Query("DELETE FROM controller_settings_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM controller_settings_data WHERE id = :id")
    suspend fun getById(id: String): ControllerSettingsDataEntity?

    @Query("SELECT * FROM controller_settings_data")
    suspend fun getAll(): List<ControllerSettingsDataEntity>

    @Query("SELECT * FROM controller_settings_data ORDER BY time ASC, rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<ControllerSettingsDataEntity>
}
