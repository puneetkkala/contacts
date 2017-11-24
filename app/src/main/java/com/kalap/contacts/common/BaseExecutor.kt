package com.kalap.contacts.common

import java.util.concurrent.Executors

open class BaseExecutor {
    protected fun runButNotOnMainThread(toRun: Runnable, notOn: Thread) {
        if (Thread.currentThread() === notOn) THREADPOOL.submit(toRun) else toRun.run()
    }

    companion object {
        private val THREADPOOL = Executors.newCachedThreadPool()
    }
}