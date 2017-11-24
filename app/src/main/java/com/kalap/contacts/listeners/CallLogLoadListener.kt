package com.kalap.contacts.listeners

import com.kalap.contacts.`object`.PhoneLog

import java.util.ArrayList

interface CallLogLoadListener {
    fun onCallLogLoaded(phoneLogs: ArrayList<PhoneLog>)
}
