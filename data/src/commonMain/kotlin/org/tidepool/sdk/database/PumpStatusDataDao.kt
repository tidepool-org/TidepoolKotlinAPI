package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.PumpStatusDataEntity

// Not yet wired into LoopKitDatabase: it gets its table in the outbox's v3->v4 migration.
@Dao
interface PumpStatusDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: PumpStatusDataEntity)

    @Delete
    suspend fun delete(data: PumpStatusDataEntity)

    @Delete
    suspend fun delete(data: List<PumpStatusDataEntity>)

    @Query("SELECT * FROM pump_status_data WHERE id = :id")
    suspend fun getById(id: String): PumpStatusDataEntity?

    @Query("SELECT * FROM pump_status_data")
    suspend fun getAll(): List<PumpStatusDataEntity>

    @Query("SELECT * FROM pump_status_data ORDER BY time ASC, rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<PumpStatusDataEntity>

    @Query("DELETE FROM pump_status_data")
    suspend fun deleteAll()
}
