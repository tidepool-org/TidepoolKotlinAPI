package org.tidepool.sdk.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.test.runTest
import org.tidepool.sdk.database.entity.data.FoodDataEntity
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runs the outbox DAO queries against real SQLite, which the in-memory fakes in commonTest can't
 * check. FoodDataDao stands in for all six outbox DAOs: they share the same `getAll(limit)` query
 * and list `@Delete`. Shared by a host JVM unit test and an on-device instrumented test, which only
 * differ in where the [Context] comes from.
 */
abstract class FoodDataDaoRoomContract {

    protected abstract fun context(): Context

    private lateinit var database: LoopKitDatabase
    private lateinit var dao: FoodDataDao

    @BeforeTest
    fun openDatabase() {
        database = Room.inMemoryDatabaseBuilder<LoopKitDatabase>(context())
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = database.foodDataDao()
    }

    @AfterTest
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun getAllWithLimitSortsNullTimeFirstThenTimeThenInsertionOrder() = runTest {
        insert("late", time = 300)
        insert("tie-b", time = 200)
        insert("no-time", time = null)
        insert("tie-a", time = 200)
        insert("early", time = 100)

        assertEquals(
            listOf("no-time", "early", "tie-b", "tie-a", "late"),
            dao.getAll(limit = 10).map { it.id },
        )
    }

    @Test
    fun getAllWithLimitReturnsOnlyTheOldestRows() = runTest {
        insert("third", time = 300)
        insert("first", time = 100)
        insert("fourth", time = 400)
        insert("second", time = 200)

        assertEquals(listOf("first", "second"), dao.getAll(limit = 2).map { it.id })
    }

    @Test
    fun listDeleteRemovesExactlyTheGivenRows() = runTest {
        insert("a", time = 100)
        insert("b", time = 200)
        insert("c", time = 300)
        val toDelete = dao.getAll().filter { it.id != "b" }

        dao.delete(toDelete)

        assertEquals(listOf("b"), dao.getAll().map { it.id })
    }

    private suspend fun insert(id: String, time: Long?) = dao.insert(
        FoodDataEntity(id = id, type = "\"food\"", time = time, timeZoneOffset = null),
    )
}
