package com.kalap.contacts.common

import android.content.Context

class Preferences(val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("contactsPreferences", Context.MODE_PRIVATE)

    fun getLong(key: String, def: Long = 0): Long = sharedPreferences.getLong(key, def)

    fun putLong(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()
}