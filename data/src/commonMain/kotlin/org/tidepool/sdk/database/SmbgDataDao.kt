package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.SmbgDataEntity

@Dao
interface SmbgDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SmbgDataEntity)

    @Delete
    suspend fun delete(data: SmbgDataEntity)

    @Delete
    suspend fun delete(data: List<SmbgDataEntity>)

    @Query("SELECT * FROM smbg_data WHERE id = :id")
    suspend fun getById(id: String): SmbgDataEntity?

    @Query("SELECT * FROM smbg_data")
    suspend fun getAll(): List<SmbgDataEntity>

    @Query("SELECT * FROM smbg_data ORDER BY time ASC, rowid ASC LIMIT :limit")
    suspend fun getAll(limit: Int): List<SmbgDataEntity>

    @Query("DELETE FROM smbg_data")
    suspend fun deleteAll()
}
