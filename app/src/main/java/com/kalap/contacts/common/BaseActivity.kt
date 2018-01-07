package com.kalap.contacts.common

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    abstract fun getLayoutId(): Int

    protected lateinit var pref: Preferences

    protected fun requestPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        pref = Preferences(this)
        initUi()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionResult(requestCode, grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)
    }

    abstract fun permissionResult(requestCode: Int, granted: Boolean)

    abstract fun initUi()
}