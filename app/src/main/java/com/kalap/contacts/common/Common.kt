package com.kalap.contacts.common

import android.content.Context
import android.view.Gravity.CENTER
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.crashlytics.android.Crashlytics
import com.kalap.contacts.BuildConfig

object Common {
    const val REQUEST_READ_CONTACT_PERMISSION = 101
    const val LAST_FETCH_TIME = "lastFetchTime"
    const val MINS_5 = 0x493E0L

    fun Throwable.log() {
        if (BuildConfig.DEBUG) this.printStackTrace() else Crashlytics.logException(this)
    }

    fun Int.longToast(context: Context) {
        val  toast = Toast.makeText(context, this, LENGTH_LONG)
        toast.setGravity(CENTER, 0, 0)
        toast.show()
    }
}