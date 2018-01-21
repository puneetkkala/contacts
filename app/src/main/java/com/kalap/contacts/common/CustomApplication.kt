package com.kalap.contacts.common

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.kalap.contacts.common.Common.log
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

class CustomApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Thread.setDefaultUncaughtExceptionHandler { _, e -> e.log() }
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("contacts.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}