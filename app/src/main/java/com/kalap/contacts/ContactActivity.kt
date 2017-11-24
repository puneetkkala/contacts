package com.kalap.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.kalap.contacts.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_contact.*

import java.util.ArrayList

class ContactActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private var data: Uri? = null
    private var prevMenuItem: MenuItem? = null

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        data = intent.data
        setContentView(R.layout.activity_contact)
        if (ContextCompat.checkSelfPermission(this@ContactActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@ContactActivity, arrayOf(Manifest.permission.CALL_PHONE), CALL_PHONE_REQUEST)
        }
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        val fragments = ArrayList<Fragment>()
        val dialerFragment = DialerFragment.newInstance(data)
        fragments.add(dialerFragment)
        val callLogsFragment = CallLogsFragment.newInstance()
        fragments.add(callLogsFragment)
        val contactListFragment = ContactListFragment.newInstance()
        fragments.add(contactListFragment)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        fragment_container.adapter = adapter
        fragment_container.addOnPageChangeListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (prevMenuItem != null) {
            prevMenuItem!!.isChecked = false
        } else {
            bottom_navigation.menu.getItem(0).isChecked = false
        }
        bottom_navigation.menu.getItem(position).isChecked = true
        prevMenuItem = bottom_navigation.menu.getItem(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    companion object {
        private val CALL_PHONE_REQUEST = 102
    }
}
