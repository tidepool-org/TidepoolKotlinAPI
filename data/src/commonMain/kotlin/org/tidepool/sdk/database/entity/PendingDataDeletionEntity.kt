package org.tidepool.sdk.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** A deletion waiting in the outbox: the origin id of the datum to remove from Tidepool. */
@Entity(tableName = "pending_data_deletion")
data class PendingDataDeletionEntity(
    @PrimaryKey
    @ColumnInfo(name = "origin_id")
    val originId: String,
)
