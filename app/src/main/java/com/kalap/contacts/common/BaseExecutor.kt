package com.kalap.contacts.common

import android.content.Context
import android.os.Looper
import java.util.concurrent.Executors

open class BaseExecutor(context: Context) {

    protected val pref = Preferences(context)

    protected fun runOnBackgroundThread(toRun: Runnable, notOn: Thread = Looper.getMainLooper().thread) {
        if (Thread.currentThread() == notOn) THREADPOOL.submit(toRun) else toRun.run()
    }

    companion object {
        private val THREADPOOL = Executors.newCachedThreadPool()
    }
}