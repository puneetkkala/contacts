package com.kalap.contacts.common

import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import java.util.concurrent.Executors

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val pref = Preferences(application)

    protected val context: Context
        get() = getApplication()

    protected fun runOnBackgroundThread(toRun: Runnable, notOn: Thread = Looper.getMainLooper().thread) {
        if (Thread.currentThread() == notOn) THREADPOOL.submit(toRun) else toRun.run()
    }

    companion object {
        private val THREADPOOL = Executors.newCachedThreadPool()
    }
}