package com.kalap.contacts

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kalap.contacts.calllogs.CallLogsFragment
import com.kalap.contacts.common.longToast
import com.kalap.contacts.common.onPageSelected
import com.kalap.contacts.contact.ContactListFragment
import com.kalap.contacts.dialer.DialerFragment
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity(R.layout.activity_contact) {

    private var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        checkPermission()
        bottom_navigation.setOnNavigationItemSelectedListener(::onNavigationItemSelected)
        val dialerFragment = DialerFragment.newInstance(intent.data)
        val callLogsFragment = CallLogsFragment()
        val contactListFragment = ContactListFragment()
        val fragments = arrayListOf<Fragment>() + dialerFragment + callLogsFragment + contactListFragment
        val adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        fragment_container.adapter = adapter
        fragment_container.onPageSelected(::onPageSelected)
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_dial -> {
                fragment_container.currentItem = 0
                return true
            }
            R.id.action_recent -> {
                fragment_container.currentItem = 1
                return true
            }
            R.id.action_all -> {
                fragment_container.currentItem = 2
                return true
            }
        }
        return false
    }

    private fun onPageSelected(position: Int) {
        if (prevMenuItem != null) {
            prevMenuItem!!.isChecked = false
        } else {
            bottom_navigation.menu.getItem(0).isChecked = false
        }
        bottom_navigation.menu.getItem(position).isChecked = true
        prevMenuItem = bottom_navigation.menu.getItem(position)
    }

    private fun checkPermission() {
        val permissions = arrayOf(READ_CONTACTS, CALL_PHONE, READ_CALL_LOG)
        val shouldRequest = permissions.any { checkSelfPermission(it) != PERMISSION_GRANTED }
        if (shouldRequest) {
            requestPermissions(permissions, 101)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults.any { it != PERMISSION_GRANTED }) {
            onPermissionRejected()
        }
    }

    private fun onPermissionRejected() {
        longToast(R.string.permission_text)
    }
}

class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
}
