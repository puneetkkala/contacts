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
import kotlinx.android.synthetic.main.dialer_fragment.view.*
import java.util.*

class DialerFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, T9Executor.T9SearchCompleteListener {

    private var phoneNumberStr: String = ""
    private var allContacts: ArrayList<Contact>? = null
    private var adapter: ContactAdapter? = null
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.dialer_fragment, container, false)!!
        root._1.setOnClickListener(this)
        root._2.setOnClickListener(this)
        root._3.setOnClickListener(this)
        root._4.setOnClickListener(this)
        root._5.setOnClickListener(this)
        root._6.setOnClickListener(this)
        root._7.setOnClickListener(this)
        root._8.setOnClickListener(this)
        root._9.setOnClickListener(this)
        root._0.setOnClickListener(this)
        root._0.setOnLongClickListener(this)
        root._star.setOnClickListener(this)
        root._hash.setOnClickListener(this)
        root._call.setOnClickListener(this)
        root.backspace.setOnClickListener(this)
        root.backspace.setOnLongClickListener(this)
        root.phone_number.setOnClickListener(this)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(root.windowToken, InputMethodManager.RESULT_UNCHANGED_HIDDEN)
        val b = arguments
        if (b != null) {
            var data = b.getString("data")
            if (data != null) {
                data = data.substring(4)
                phoneNumberStr = data
                root.phone_number.text = phoneNumberStr
            }
        }
        val helper = ContactsDatabaseHelper()
        allContacts = helper.getAllContacts()
        root.contacts_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = ContactAdapter(activity, TreeMap())
        root.contacts_rv.adapter = adapter
        return root
    }

    private fun matchPattern() {
        val executor = T9Executor(activity.baseContext)
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
        root.phone_number.text = phoneNumberStr
    }

    private fun reset() {
        phoneNumberStr = ""
        root.phone_number.text = ""
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
