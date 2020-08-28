package com.kalap.contacts.common

import android.content.Context
import com.kalap.contacts.BuildConfig

object PrefKeys {
    const val LAST_FETCH_TIME = "last.fetch.time.long"
}

class Preferences(val context: Context) {

    private val prefName = "${BuildConfig.APPLICATION_ID}.pref"
    private val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getLong(key: String, def: Long = 0): Long = sharedPreferences.getLong(key, def)
    fun putLong(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()
}