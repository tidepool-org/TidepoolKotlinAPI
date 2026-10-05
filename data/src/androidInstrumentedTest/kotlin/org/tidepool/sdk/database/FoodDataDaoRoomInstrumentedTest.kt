package org.tidepool.sdk.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider

/** On-device run of [FoodDataDaoRoomContract], against the same SQLite build the app ships. */
class FoodDataDaoRoomInstrumentedTest : FoodDataDaoRoomContract() {
    override fun context(): Context = ApplicationProvider.getApplicationContext()
}
