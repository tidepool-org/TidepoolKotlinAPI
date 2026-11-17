package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.ControllerStatusDataEntity

@Dao
interface ControllerStatusDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ControllerStatusDataEntity)

    @Delete
    suspend fun delete(data: ControllerStatusDataEntity)

    @Delete
    suspend fun delete(data: List<ControllerStatusDataEntity>)

    @Query("SELECT * FROM controller_status_data WHERE id = :id")
    suspend fun getById(id: String): ControllerStatusDataEntity?

    @Query("SELECT * FROM controller_status_data")
    suspend fun getAll(): List<ControllerStatusDataEntity>

    @Query("SELECT * FROM controller_status_data ORDER BY time ASC, rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<ControllerStatusDataEntity>

    @Query("DELETE FROM controller_status_data")
    suspend fun deleteAll()
}
