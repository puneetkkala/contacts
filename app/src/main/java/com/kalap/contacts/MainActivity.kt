package com.kalap.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.crashlytics.android.Crashlytics
import com.kalap.contacts.executors.ContactExecutor
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_READ_CONTACT_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    execute()
                }
            }
        }
    }

    private fun execute() {
        val executor = ContactExecutor()
        executor.loadContacts(this)
        startActivity(Intent(this@MainActivity, ContactActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Crashlytics.logException(e)
            e.printStackTrace()
        }
        setContentView(R.layout.activity_main)
        loading_contacts_bar.visibility = View.VISIBLE
        try {

            val sharedPreferences = getSharedPreferences("contactsPreferences", Context.MODE_PRIVATE)
            val time = sharedPreferences.getLong("lastFetchTime", 0)
            if (System.currentTimeMillis() > time + 7 * 24 * 60 * 60 * 1000) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_CONTACTS), MY_READ_CONTACT_PERMISSION_REQUEST)
                } else {
                    execute()
                }
            } else {
                preparing_text_view.text = "Loading..."
                loading_contacts_bar.visibility = View.INVISIBLE
                startActivity(Intent(this@MainActivity, ContactActivity::class.java))
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val MY_READ_CONTACT_PERMISSION_REQUEST = 101
    }
}
