package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity

@Dao
interface BasalAutomatedDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: BasalAutomatedDataEntity)

    @Delete
    suspend fun delete(data: BasalAutomatedDataEntity)

    @Query("SELECT * FROM basal_automated_data WHERE id = :id")
    suspend fun getById(id: String): BasalAutomatedDataEntity?

    @Query("SELECT * FROM basal_automated_data")
    suspend fun getAll(): List<BasalAutomatedDataEntity>
}
