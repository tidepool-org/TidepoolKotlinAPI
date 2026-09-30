package org.tidepool.sdk.repository

import org.tidepool.sdk.database.BasalAutomatedDataDao
import org.tidepool.sdk.database.BolusDataDao
import org.tidepool.sdk.database.CgmSettingsDataDao
import org.tidepool.sdk.database.ContinuousGlucoseDataDao
import org.tidepool.sdk.database.ControllerSettingsDataDao
import org.tidepool.sdk.database.DeviceEventDataDao
import org.tidepool.sdk.database.DosingDecisionDataDao
import org.tidepool.sdk.database.FoodDataDao
import org.tidepool.sdk.database.InsulinDataDao
import org.tidepool.sdk.database.PumpSettingsDataDao
import org.tidepool.sdk.database.entity.data.BaseDataEntity
import org.tidepool.sdk.database.entity.data.BasalAutomatedDataEntity
import org.tidepool.sdk.database.entity.data.BolusDataEntity
import org.tidepool.sdk.database.entity.data.CgmSettingsDataEntity
import org.tidepool.sdk.database.entity.data.ContinuousGlucoseDataEntity
import org.tidepool.sdk.database.entity.data.ControllerSettingsDataEntity
import org.tidepool.sdk.database.entity.data.DeviceEventDataEntity
import org.tidepool.sdk.database.entity.data.DosingDecisionDataEntity
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import org.tidepool.sdk.database.entity.data.InsulinDataEntity
import org.tidepool.sdk.database.entity.data.PumpSettingsDataEntity

/**
 * One outbox table kept in insertion order, standing in for Room. `getAll(limit)` takes the first
 * rows, like the real `ORDER BY time ASC, rowid ASC LIMIT` does for rows inserted in time order.
 */
internal class InMemoryTable<T : BaseDataEntity> {
    val rows = mutableListOf<T>()

    fun insert(row: T) {
        rows.removeAll { it.id == row.id }
        rows += row
    }

    fun delete(row: T) {
        rows.remove(row)
    }

    fun delete(batch: List<T>) {
        rows.removeAll(batch.toSet())
    }

    fun getById(id: String) = rows.find { it.id == id }
    fun getAll() = rows.toList()
    fun getAll(limit: Int) = rows.take(limit)
}

internal class InMemoryBasalAutomatedDataDao : BasalAutomatedDataDao {
    val table = InMemoryTable<BasalAutomatedDataEntity>()
    override suspend fun insert(data: BasalAutomatedDataEntity) = table.insert(data)
    override suspend fun delete(data: BasalAutomatedDataEntity) = table.delete(data)
    override suspend fun delete(data: List<BasalAutomatedDataEntity>) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
    override suspend fun getAll(limit: Int) = table.getAll(limit)
}

internal class InMemoryBolusDataDao : BolusDataDao {
    val table = InMemoryTable<BolusDataEntity>()
    override suspend fun insert(data: BolusDataEntity) = table.insert(data)
    override suspend fun delete(data: BolusDataEntity) = table.delete(data)
    override suspend fun delete(data: List<BolusDataEntity>) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
    override suspend fun getAll(limit: Int) = table.getAll(limit)
}

internal class InMemoryContinuousGlucoseDataDao : ContinuousGlucoseDataDao {
    val table = InMemoryTable<ContinuousGlucoseDataEntity>()
    override suspend fun insert(data: ContinuousGlucoseDataEntity) = table.insert(data)
    override suspend fun delete(data: ContinuousGlucoseDataEntity) = table.delete(data)
    override suspend fun delete(data: List<ContinuousGlucoseDataEntity>) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
    override suspend fun getAll(limit: Int) = table.getAll(limit)
}

internal class InMemoryDosingDecisionDataDao : DosingDecisionDataDao {
    val table = InMemoryTable<DosingDecisionDataEntity>()
    override suspend fun insert(data: DosingDecisionDataEntity) = table.insert(data)
    override suspend fun delete(data: DosingDecisionDataEntity) = table.delete(data)
    override suspend fun delete(data: List<DosingDecisionDataEntity>) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
    override suspend fun getAll(limit: Int) = table.getAll(limit)
}

internal class InMemoryFoodDataDao : FoodDataDao {
    val table = InMemoryTable<FoodDataEntity>()
    override suspend fun insert(data: FoodDataEntity) = table.insert(data)
    override suspend fun delete(data: FoodDataEntity) = table.delete(data)
    override suspend fun delete(data: List<FoodDataEntity>) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
    override suspend fun getAll(limit: Int) = table.getAll(limit)
}

internal class InMemoryInsulinDataDao : InsulinDataDao {
    val table = InMemoryTable<InsulinDataEntity>()
    override suspend fun insert(data: InsulinDataEntity) = table.insert(data)
    override suspend fun delete(data: InsulinDataEntity) = table.delete(data)
    override suspend fun delete(data: List<InsulinDataEntity>) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
    override suspend fun getAll(limit: Int) = table.getAll(limit)
}

internal class InMemoryDeviceEventDataDao : DeviceEventDataDao {
    val table = InMemoryTable<DeviceEventDataEntity>()
    override suspend fun insert(data: DeviceEventDataEntity) = table.insert(data)
    override suspend fun delete(data: DeviceEventDataEntity) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
}

internal class InMemoryCgmSettingsDataDao : CgmSettingsDataDao {
    val table = InMemoryTable<CgmSettingsDataEntity>()
    override suspend fun insert(data: CgmSettingsDataEntity) = table.insert(data)
    override suspend fun delete(data: CgmSettingsDataEntity) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
}

internal class InMemoryControllerSettingsDataDao : ControllerSettingsDataDao {
    val table = InMemoryTable<ControllerSettingsDataEntity>()
    override suspend fun insert(data: ControllerSettingsDataEntity) = table.insert(data)
    override suspend fun delete(data: ControllerSettingsDataEntity) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
}

internal class InMemoryPumpSettingsDataDao : PumpSettingsDataDao {
    val table = InMemoryTable<PumpSettingsDataEntity>()
    override suspend fun insert(data: PumpSettingsDataEntity) = table.insert(data)
    override suspend fun delete(data: PumpSettingsDataEntity) = table.delete(data)
    override suspend fun getById(id: String) = table.getById(id)
    override suspend fun getAll() = table.getAll()
}

internal class InMemoryKeyValueStorage : KeyValueStorage {
    private val values = mutableMapOf<String, Any?>()

    override fun getString(key: String) = values[key] as String?
    override fun putString(key: String, value: String?) = put(key, value)
    override fun getInt(key: String) = values[key] as Int?
    override fun putInt(key: String, value: Int?) = put(key, value)
    override fun getLong(key: String) = values[key] as Long?
    override fun putLong(key: String, value: Long?) = put(key, value)
    override fun getFloat(key: String) = values[key] as Float?
    override fun putFloat(key: String, value: Float?) = put(key, value)
    override fun getBoolean(key: String) = values[key] as Boolean?
    override fun putBoolean(key: String, value: Boolean?) = put(key, value)

    private fun put(key: String, value: Any?) {
        values[key] = value
    }
}
