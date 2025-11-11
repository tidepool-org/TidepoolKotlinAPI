package org.tidepool.sdk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity
import org.tidepool.sdk.database.entity.data.BaseDataEntity
import org.tidepool.sdk.database.entity.data.BolusDataEntity
import org.tidepool.sdk.database.entity.data.CgmSettingsDataEntity
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity
import org.tidepool.sdk.database.entity.data.ControllerSettingsDataEntity
import org.tidepool.sdk.database.entity.data.DeviceEventDataEntity
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import org.tidepool.sdk.database.entity.data.InsulinDataEntity
import org.tidepool.sdk.database.entity.data.PumpSettingsDataEntity

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
        DeviceEventDataEntity::class,
        CgmSettingsDataEntity::class,
        ControllerSettingsDataEntity::class,
        PumpSettingsDataEntity::class,
    ],
)
@TypeConverters(DatabaseConverters::class)
abstract class LoopKitDatabase : RoomDatabase() {
    abstract fun basalAutomatedDataDao(): BasalAutomatedDataDao
    abstract fun bolusDataDao(): BolusDataDao
    abstract fun continuousGlucoseDataDao(): ContinuousGlucoseDataDao
    abstract fun dosingDecisionDataDao(): DosingDecisionDataDao
    abstract fun foodDataDao(): FoodDataDao
    abstract fun insulinDataDao(): InsulinDataDao
    abstract fun deviceEventDataDao(): DeviceEventDataDao
    abstract fun cgmSettingsDataDao(): CgmSettingsDataDao
    abstract fun controllerSettingsDataDao(): ControllerSettingsDataDao
    abstract fun pumpSettingsDataDao(): PumpSettingsDataDao
}