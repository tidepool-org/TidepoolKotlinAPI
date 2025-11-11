package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.CgmSettingsDataEntity

@Dao
interface CgmSettingsDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: CgmSettingsDataEntity)

    @Delete
    suspend fun delete(data: CgmSettingsDataEntity)

    @Query("SELECT * FROM cgm_settings_data WHERE id = :id")
    suspend fun getById(id: String): CgmSettingsDataEntity?

    @Query("SELECT * FROM cgm_settings_data")
    suspend fun getAll(): List<CgmSettingsDataEntity>
}
