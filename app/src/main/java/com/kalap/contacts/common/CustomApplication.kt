package com.kalap.contacts.common

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class CustomApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("contacts.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}