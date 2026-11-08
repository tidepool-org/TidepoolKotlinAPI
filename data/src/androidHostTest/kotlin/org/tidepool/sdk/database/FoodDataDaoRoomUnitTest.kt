package org.tidepool.sdk.database

import android.content.Context
import io.mockk.mockk

/**
 * Host JVM run of [FoodDataDaoRoomContract]. An in-memory database with the bundled driver never
 * reads from the Context, so a relaxed mock is enough. The native SQLite library comes from
 * `sqlite-bundled-jvm`, since the Android artifact only ships Android binaries.
 */
class FoodDataDaoRoomUnitTest : FoodDataDaoRoomContract() {
    override fun context(): Context = mockk(relaxed = true)
}
