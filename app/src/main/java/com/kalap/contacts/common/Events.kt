package com.kalap.contacts.common

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.kalap.contacts.BuildConfig

/**
 * Created by kalapuneet on 07-11-2017.
 */
class Events {
    companion object {
        fun postCallEvent(source: String) {
            try {
                Answers.getInstance().logCustom(CustomEvent("CALL BUTTON CLICKED")
                        .putCustomAttribute("source", source)
                        .putCustomAttribute("version", BuildConfig.VERSION_NAME))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}