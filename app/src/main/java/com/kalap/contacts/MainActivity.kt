package com.kalap.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kalap.contacts.common.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG)
        val shouldRequest = permissions.any { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
        if (shouldRequest) {
            requestPermissions(permissions, 101)
        } else {
            onPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            onPermissionRejected()
        } else {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        navigateTo<ContactActivity>(true)
    }

    private fun onPermissionRejected() {
        longToast(R.string.permission_text)
    }
}
