package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.InsulinDataEntity

@Dao
interface InsulinDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: InsulinDataEntity)

    @Delete
    suspend fun delete(data: InsulinDataEntity)

    @Query("SELECT * FROM insulin_data WHERE id = :id")
    suspend fun getById(id: String): InsulinDataEntity?

    @Query("SELECT * FROM insulin_data")
    suspend fun getAll(): List<InsulinDataEntity>
}
