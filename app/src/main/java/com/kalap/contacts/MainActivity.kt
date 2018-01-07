package com.kalap.contacts

import android.Manifest
import android.content.Intent
import android.view.View
import com.kalap.contacts.common.BaseActivity
import com.kalap.contacts.common.Common
import com.kalap.contacts.executors.ContactExecutor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initUi() {
        loadingContactsBar.visibility = View.VISIBLE
        try {
            val time = pref.getLong(Common.LAST_FETCH_TIME)
            if (System.currentTimeMillis() > time + Common.MINS_5) {
                if (requestPermission(Manifest.permission.READ_CONTACTS, Common.REQUEST_READ_CONTACT_PERMISSION)) {
                    execute()
                }
            } else {
                preparingTv.text = getString(R.string.loading)
                loadingContactsBar.visibility = View.INVISIBLE
                startContactActivity()
            }
        } catch (e: Exception) {
            Common.err(e)
        }
    }

    private fun execute() {
        ContactExecutor(this).loadContacts()
        startContactActivity()
    }

    override fun permissionResult(requestCode: Int, granted: Boolean) {
        when (requestCode) {
            Common.REQUEST_READ_CONTACT_PERMISSION -> {
                if (granted) {
                    execute()
                } else {
                    Common.longToast(this, getString(R.string.permission_text))
                }
            }
        }
    }

    private fun startContactActivity() {
        startActivity(Intent(this, ContactActivity::class.java))
        finish()
    }
}
