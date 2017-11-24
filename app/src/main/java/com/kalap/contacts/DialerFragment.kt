package com.kalap.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.adapters.ContactAdapter
import com.kalap.contacts.database.ContactsDatabaseHelper
import com.kalap.contacts.executors.T9Executor
import kotlinx.android.synthetic.main.dialer_fragment.*
import java.util.*

class DialerFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, T9Executor.T9SearchCompleteListener {

    private var phoneNumberStr: String = ""
    private var allContacts: ArrayList<Contact>? = null
    private var adapter: ContactAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.dialer_fragment, container, false)
        _1.setOnClickListener(this)
        _2.setOnClickListener(this)
        _3.setOnClickListener(this)
        _4.setOnClickListener(this)
        _5.setOnClickListener(this)
        _6.setOnClickListener(this)
        _7.setOnClickListener(this)
        _8.setOnClickListener(this)
        _9.setOnClickListener(this)
        _0.setOnClickListener(this)
        _0.setOnLongClickListener(this)
        _star.setOnClickListener(this)
        _hash.setOnClickListener(this)
        _call.setOnClickListener(this)
        backspace.setOnClickListener(this)
        backspace.setOnLongClickListener(this)
        phone_number.setOnClickListener(this)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_HIDDEN)
        val b = arguments
        if (b != null) {
            var data = b.getString("data")
            if (data != null) {
                data = data.substring(4)
                phoneNumberStr = data
                phone_number.text = phoneNumberStr
            }
        }
        val helper = ContactsDatabaseHelper(activity)
        allContacts = helper.allContacts
        contacts_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = ContactAdapter(activity, TreeMap())
        contacts_rv.adapter = adapter
        return view
    }

    private fun matchPattern() {
        val executor = T9Executor()
        executor.listener = this
        executor.getT9Contacts(allContacts, phoneNumberStr)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id._1 -> {
                phoneNumberStr += "1"
            }
            R.id._2 -> {
                phoneNumberStr += "2"
            }
            R.id._3 -> {
                phoneNumberStr += "3"
            }
            R.id._4 -> {
                phoneNumberStr += "4"
            }
            R.id._5 -> {
                phoneNumberStr += "5"
            }
            R.id._6 -> {
                phoneNumberStr += "6"
            }
            R.id._7 -> {
                phoneNumberStr += "7"
            }
            R.id._8 -> {
                phoneNumberStr += "8"
            }
            R.id._9 -> {
                phoneNumberStr += "9"
            }
            R.id._0 -> {
                phoneNumberStr += "0"
            }
            R.id._star -> {
                phoneNumberStr += "*"
            }
            R.id._hash -> {
                phoneNumberStr += "#"
            }
            R.id._call -> {
                if (phoneNumberStr.isNotEmpty()) {
                    try {
                        Answers.getInstance().logCustom(CustomEvent("CALL BUTTON CLICKED")
                                .putCustomAttribute("source", "DialerFragment")
                                .putCustomAttribute("version", BuildConfig.VERSION_NAME))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val data = "tel:" + phoneNumberStr
                    val callIntent = Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    callIntent.data = Uri.parse(data)
                    activity.startActivity(callIntent)
                    reset()
                }
            }
            R.id.backspace -> {
                if (phoneNumberStr.isNotEmpty()) phoneNumberStr = phoneNumberStr.substring(0, phoneNumberStr.length - 1)
            }
        }
        matchPattern()
        phone_number.text = phoneNumberStr
    }

    private fun reset() {
        phoneNumberStr = ""
        phone_number.text = ""
    }

    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            R.id.backspace -> {
                reset()
                matchPattern()
                return true
            }
            R.id._0 -> {
                phoneNumberStr += "+"
                return true
            }
        }
        return false
    }

    override fun t9SearchCompleted(displayContacts: TreeMap<String, Contact>) {
        activity.runOnUiThread {
            adapter?.updateData(displayContacts)
        }
    }

    companion object {
        fun newInstance(uri: Uri?): DialerFragment {
            val args = Bundle()
            if (uri != null) args.putString("data", uri.toString())
            val fragment = DialerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
