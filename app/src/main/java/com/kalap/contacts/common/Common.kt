package com.kalap.contacts.common

import android.content.Context
import android.view.Gravity.CENTER
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.crashlytics.android.Crashlytics
import com.kalap.contacts.BuildConfig

object Common {
    const val REQUEST_READ_CONTACT_PERMISSION = 101
    const val DAYS_7 = 0x240C8400L
    const val LAST_FETCH_TIME = "lastFetchTime"
    const val MINS_5 = 0x493E0L

    fun err(e: Throwable) = if (BuildConfig.DEBUG) e.printStackTrace() else Crashlytics.logException(e)

    fun longToast(context: Context, text: String) {
        val toast = Toast.makeText(context, text, LENGTH_LONG)
        toast.setGravity(CENTER, 0, 0)
        toast.show()
    }
}