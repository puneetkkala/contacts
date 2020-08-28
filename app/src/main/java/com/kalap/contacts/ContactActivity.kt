package com.kalap.contacts

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kalap.contacts.calllogs.CallLogsFragment
import com.kalap.contacts.contact.ContactListFragment
import com.kalap.contacts.dialer.DialerFragment
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity(R.layout.activity_contact), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private var prevMenuItem: MenuItem? = null

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        val dialerFragment = DialerFragment.newInstance(intent.data)
        val callLogsFragment = CallLogsFragment()
        val contactListFragment = ContactListFragment()
        val fragments = arrayListOf<Fragment>() + dialerFragment + callLogsFragment + contactListFragment
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
}

class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int = fragments.size
}
