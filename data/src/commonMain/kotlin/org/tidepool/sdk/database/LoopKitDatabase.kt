package org.tidepool.sdk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.tidepool.sdk.database.entity.PendingDataDeletionEntity
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity
import org.tidepool.sdk.database.entity.data.BaseDataEntity
import org.tidepool.sdk.database.entity.data.BolusDataEntity
import org.tidepool.sdk.database.entity.data.CgmSettingsDataEntity
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity
import org.tidepool.sdk.database.entity.data.ControllerSettingsDataEntity
import org.tidepool.sdk.database.entity.data.ControllerStatusDataEntity
import org.tidepool.sdk.database.entity.data.DeviceEventDataEntity
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import org.tidepool.sdk.database.entity.data.InsulinDataEntity
import org.tidepool.sdk.database.entity.data.PumpSettingsDataEntity
import org.tidepool.sdk.database.entity.data.PumpStatusDataEntity
import org.tidepool.sdk.database.entity.data.SmbgDataEntity

@Database(
    version = 5,
    exportSchema = true,
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
        SmbgDataEntity::class,
        PumpStatusDataEntity::class,
        ControllerStatusDataEntity::class,
        PendingDataDeletionEntity::class,
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
    abstract fun smbgDataDao(): SmbgDataDao
    abstract fun pumpStatusDataDao(): PumpStatusDataDao
    abstract fun controllerStatusDataDao(): ControllerStatusDataDao
    abstract fun pendingDataDeletionDao(): PendingDataDeletionDao
}
