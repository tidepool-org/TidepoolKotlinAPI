package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity

@Dao
interface ContinuousGlucoseDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ContinuousGlucoseDataEntity)

    @Delete
    suspend fun delete(data: ContinuousGlucoseDataEntity)

    @Delete
    suspend fun delete(data: List<ContinuousGlucoseDataEntity>)

    @Query("SELECT * FROM continuous_glucose_data WHERE id = :id")
    suspend fun getById(id: String): ContinuousGlucoseDataEntity?

    @Query("SELECT * FROM continuous_glucose_data")
    suspend fun getAll(): List<ContinuousGlucoseDataEntity>

    @Query("SELECT * FROM continuous_glucose_data ORDER BY time ASC, rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<ContinuousGlucoseDataEntity>
}
