package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.PendingDataDeletionEntity

@Dao
interface PendingDataDeletionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<PendingDataDeletionEntity>)

    @Delete
    suspend fun delete(data: List<PendingDataDeletionEntity>)

    @Query("SELECT * FROM pending_data_deletion ORDER BY rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<PendingDataDeletionEntity>
}
