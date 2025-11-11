package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity

@Dao
interface DosingDecisionDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DosingDecisionDataEntity)

    @Delete
    suspend fun delete(data: DosingDecisionDataEntity)

    @Query("SELECT * FROM dosing_decision_data WHERE id = :id")
    suspend fun getById(id: String): DosingDecisionDataEntity?

    @Query("SELECT * FROM dosing_decision_data")
    suspend fun getAll(): List<DosingDecisionDataEntity>
}
