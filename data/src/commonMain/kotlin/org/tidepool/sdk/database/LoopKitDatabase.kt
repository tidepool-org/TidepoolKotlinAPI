package org.tidepool.sdk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity
import org.tidepool.sdk.database.entity.data.BaseDataEntity
import org.tidepool.sdk.database.entity.data.BolusDataEntity
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import org.tidepool.sdk.database.entity.data.InsulinDataEntity

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        BaseDataEntity::class,
        BasalAutomatedDataEntity::class,
        BolusDataEntity::class,
        ContinuousGlucoseDataEntity::class,
        DosingDecisionDataEntity::class,
        FoodDataEntity::class,
        InsulinDataEntity::class,
    ],
)
@TypeConverters(DatabaseConverters::class)
abstract class LoopKitDatabase : RoomDatabase() {
    
    abstract fun dataDao(): DataDao
}