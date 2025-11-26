package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tidepool.sdk.database.entity.data.FoodDataEntity

@Dao
interface FoodDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: FoodDataEntity)

    @Delete
    suspend fun delete(data: FoodDataEntity)

    @Query("SELECT * FROM food_data WHERE id = :id")
    suspend fun getById(id: String): FoodDataEntity?

    @Query("SELECT * FROM food_data")
    suspend fun getAll(): List<FoodDataEntity>
}
