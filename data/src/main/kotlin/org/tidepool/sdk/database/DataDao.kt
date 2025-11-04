package org.tidepool.sdk.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity
import org.tidepool.sdk.database.entity.data.BolusDataEntity
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import org.tidepool.sdk.database.entity.data.InsulinDataEntity

@Dao
interface DataDao {
    
    // BasalAutomatedDataEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasalAutomatedData(data: BasalAutomatedDataEntity)
    
    @Update
    suspend fun updateBasalAutomatedData(data: BasalAutomatedDataEntity)
    
    @Delete
    suspend fun deleteBasalAutomatedData(data: BasalAutomatedDataEntity)
    
    @Query("SELECT * FROM basal_automated_data WHERE id = :id")
    suspend fun getBasalAutomatedDataById(id: String): BasalAutomatedDataEntity?
    
    @Query("SELECT * FROM basal_automated_data")
    suspend fun getAllBasalAutomatedData(): List<BasalAutomatedDataEntity>
    
    // BolusDataEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBolusData(data: BolusDataEntity)
    
    @Update
    suspend fun updateBolusData(data: BolusDataEntity)
    
    @Delete
    suspend fun deleteBolusData(data: BolusDataEntity)
    
    @Query("SELECT * FROM bolus_data WHERE id = :id")
    suspend fun getBolusDataById(id: String): BolusDataEntity?
    
    @Query("SELECT * FROM bolus_data")
    suspend fun getAllBolusData(): List<BolusDataEntity>
    
    // ContinuousGlucoseDataEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContinuousGlucoseData(data: ContinuousGlucoseDataEntity)
    
    @Update
    suspend fun updateContinuousGlucoseData(data: ContinuousGlucoseDataEntity)
    
    @Delete
    suspend fun deleteContinuousGlucoseData(data: ContinuousGlucoseDataEntity)
    
    @Query("SELECT * FROM continuous_glucose_data WHERE id = :id")
    suspend fun getContinuousGlucoseDataById(id: String): ContinuousGlucoseDataEntity?
    
    @Query("SELECT * FROM continuous_glucose_data")
    suspend fun getAllContinuousGlucoseData(): List<ContinuousGlucoseDataEntity>
    
    // DosingDecisionDataEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDosingDecisionData(data: DosingDecisionDataEntity)
    
    @Update
    suspend fun updateDosingDecisionData(data: DosingDecisionDataEntity)
    
    @Delete
    suspend fun deleteDosingDecisionData(data: DosingDecisionDataEntity)
    
    @Query("SELECT * FROM dosing_decision_data WHERE id = :id")
    suspend fun getDosingDecisionDataById(id: String): DosingDecisionDataEntity?
    
    @Query("SELECT * FROM dosing_decision_data")
    suspend fun getAllDosingDecisionData(): List<DosingDecisionDataEntity>
    
    // FoodDataEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodData(data: FoodDataEntity)
    
    @Update
    suspend fun updateFoodData(data: FoodDataEntity)
    
    @Delete
    suspend fun deleteFoodData(data: FoodDataEntity)
    
    @Query("SELECT * FROM food_data WHERE id = :id")
    suspend fun getFoodDataById(id: String): FoodDataEntity?
    
    @Query("SELECT * FROM food_data")
    suspend fun getAllFoodData(): List<FoodDataEntity>
    
    // InsulinDataEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsulinData(data: InsulinDataEntity)
    
    @Update
    suspend fun updateInsulinData(data: InsulinDataEntity)
    
    @Delete
    suspend fun deleteInsulinData(data: InsulinDataEntity)
    
    @Query("SELECT * FROM insulin_data WHERE id = :id")
    suspend fun getInsulinDataById(id: String): InsulinDataEntity?
    
    @Query("SELECT * FROM insulin_data")
    suspend fun getAllInsulinData(): List<InsulinDataEntity>
}