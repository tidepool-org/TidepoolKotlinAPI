package org.tidepool.sdk

import android.content.Context
import android.content.SharedPreferences
import org.tidepool.sdk.repository.KeyValueStorage

class AndroidKeyValueStorage(context: Context) : KeyValueStorage {
    private val preferences = context.getSharedPreferences("loop-kit-storage", Context.MODE_PRIVATE)

    override fun getString(key: String): String? = preferences.getString(key, null)

    override fun putString(key: String, value: String?) = preferences.edit(key, value)

    override fun getInt(key: String): Int? = if (preferences.contains(key)) {
        preferences.getInt(key, 0)
    } else {
        null
    }

    override fun putInt(key: String, value: Int?) = preferences.edit(key, value)

    override fun getLong(key: String): Long? = if (preferences.contains(key)) {
        preferences.getLong(key, 0L)
    } else {
        null
    }

    override fun putLong(key: String, value: Long?) = preferences.edit(key, value)

    override fun getFloat(key: String): Float? = if (preferences.contains(key)) {
        preferences.getFloat(key, 0f)
    } else {
        null
    }

    override fun putFloat(key: String, value: Float?) = preferences.edit(key, value)

    override fun getBoolean(key: String): Boolean? = if (preferences.contains(key)) {
        preferences.getBoolean(key, false)
    } else {
        null
    }

    override fun putBoolean(key: String, value: Boolean?) = preferences.edit(key, value)

    private fun <T : Any> SharedPreferences.edit(
        key: String,
        value: T?,
    ) = edit().apply {
        when (value) {
            null -> remove(key)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }.apply()
}
