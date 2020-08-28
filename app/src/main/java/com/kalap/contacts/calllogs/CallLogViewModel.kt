package com.kalap.contacts.calllogs

import android.app.Application
import android.provider.CallLog
import androidx.lifecycle.MutableLiveData
import com.kalap.contacts.model.PhoneLog
import com.kalap.contacts.common.BaseViewModel
import com.kalap.contacts.database.ContactsDatabaseHelper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CallLogViewModel(application: Application): BaseViewModel(application) {

    val phoneLogsLd = MutableLiveData<List<PhoneLog>>()

    fun loadCallLogs() {
        runOnBackgroundThread({ load() })
    }

    private fun load() {
        val phoneLogs = ArrayList<PhoneLog>()
        val callLogsCursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC", null)
        if (callLogsCursor != null) {
            while (callLogsCursor.moveToNext()) {
                val phoneLog = PhoneLog()
                val helper = ContactsDatabaseHelper()
                phoneLog.number = callLogsCursor.getString(callLogsCursor.getColumnIndex(CallLog.Calls.NUMBER))
                phoneLog.name = helper.getContactName(phoneLog.number)
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
                if (phoneLogs.size == 100) {
                    phoneLogsLd.postValue(phoneLogs)
                }
            }
            callLogsCursor.close()
        }
        phoneLogsLd.postValue(phoneLogs)
    }

    private fun getType(typeNum: String): String = when (Integer.valueOf(typeNum)) {
        1 -> "Incoming"
        2 -> "Outgoing"
        3 -> "Missed"
        4 -> "Voicemail"
        5 -> "Rejected"
        6 -> "Blocked"
        else -> ""
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
