package com.kalap.contacts.executors

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import android.provider.CallLog
import android.support.v4.app.ActivityCompat
import com.kalap.contacts.`object`.PhoneLog
import com.kalap.contacts.common.BaseExecutor
import com.kalap.contacts.listeners.CallLogLoadListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CallLogExecutor: BaseExecutor() {
    lateinit var listener: CallLogLoadListener

    fun loadCallLogs(context: Activity) {
        val runnable = Runnable { load(context) }
        runButNotOnMainThread(runnable, Looper.getMainLooper().thread)
    }

    private fun load(context: Activity) {
        val phoneLogs = ArrayList<PhoneLog>()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_CALL_LOG), 103)
        } else {
            val callLogsCursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC", null)
            if (callLogsCursor != null) {
                while (callLogsCursor.moveToNext()) {
                    val phoneLog = PhoneLog()
                    phoneLog.name = callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    phoneLog.number = callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.NUMBER))
                    phoneLog.type = getType(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.TYPE)))
                    val date = callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.DATE))
                    try {
                        val format = DateFormat.getDateInstance(DateFormat.FULL) as SimpleDateFormat
                        format.applyPattern("dd-MM-yyyy hh:mm:ss")
                        val localDateTime = format.format(Date(java.lang.Long.valueOf(date)!!))
                        phoneLog.date = localDateTime
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val duration = calculateDuration(callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.DURATION)))
                    phoneLog.duration = duration
                    phoneLogs.add(phoneLog)
                }
                callLogsCursor.close()
            }
            listener.onCallLogLoaded(phoneLogs)
        }
    }

    private fun getType(typeNum: String): String? {
        return when (Integer.valueOf(typeNum)) {
            1 -> "Incoming"
            2 -> "Outgoing"
            3 -> "Missed"
            4 -> "Voicemail"
            5 -> "Rejected"
            6 -> "Blocked"
            else -> null
        }
    }

    private fun calculateDuration(duration: String): String {
        val durationSec = Integer.valueOf(duration)!!
        var min = durationSec / 60
        val sec = durationSec % 60
        return when {
            min >= 60 -> {
                val hour = min / 60
                min %= 60
                hour.toString() + "h " + min + "m " + sec + "s"
            }
            min == 0 -> sec.toString() + "s"
            else -> min.toString() + "m " + sec + "s"
        }
    }
}
