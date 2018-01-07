package com.kalap.contacts.common

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

class CustomApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Thread.setDefaultUncaughtExceptionHandler { _, e -> Common.err(e) }
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("contacts.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}