package com.kalap.contacts.dialer

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.kalap.contacts.R
import com.kalap.contacts.model.Contact
import com.kalap.contacts.common.BaseFragment
import com.kalap.contacts.common.call
import com.kalap.contacts.common.keepKeyboardHidden
import com.kalap.contacts.views.ContactView
import kotlinx.android.synthetic.main.dialer_fragment.*

private const val EXTRA_DATA = "data"

class DialerFragment : BaseFragment<DialerViewModel, ContactView, Contact>(R.layout.dialer_fragment, DialerViewModel::class, ContactView::class), View.OnClickListener, View.OnLongClickListener {

    private var phoneNumberStr: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button0.setOnClickListener(this)
        button0.setOnLongClickListener(this)
        buttonStar.setOnClickListener(this)
        buttonHash.setOnClickListener(this)
        buttonCall.setOnClickListener(this)
        backspace.setOnClickListener(this)
        backspace.setOnLongClickListener(this)
        phoneNumber.setOnClickListener(this)
        view.keepKeyboardHidden()
        val b = arguments
        if (b != null) {
            var data = b.getString(EXTRA_DATA)
            if (data != null) {
                data = data.substring(4)
                phoneNumberStr = data
                phoneNumber.text = phoneNumberStr
            }
        }
        contactsRv.adapter = adapter
        viewModel.displayContactsLd.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
    }

    private fun matchPattern() {
        viewModel.getT9Contacts(phoneNumberStr)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button1 -> phoneNumberStr += "1"
            R.id.button2 -> phoneNumberStr += "2"
            R.id.button3 -> phoneNumberStr += "3"
            R.id.button4 -> phoneNumberStr += "4"
            R.id.button5 -> phoneNumberStr += "5"
            R.id.button6 -> phoneNumberStr += "6"
            R.id.button7 -> phoneNumberStr += "7"
            R.id.button8 -> phoneNumberStr += "8"
            R.id.button9 -> phoneNumberStr += "9"
            R.id.button0 -> phoneNumberStr += "0"
            R.id.buttonStar -> phoneNumberStr += "*"
            R.id.buttonHash -> phoneNumberStr += "#"
            R.id.buttonCall -> {
                if (phoneNumberStr.isNotEmpty()) {
                    context?.call(phoneNumberStr)
                }
            }
            R.id.backspace -> {
                if (phoneNumberStr.isNotEmpty()) phoneNumberStr = phoneNumberStr.substring(0, phoneNumberStr.length - 1)
            }
        }
        matchPattern()
        phoneNumber.text = phoneNumberStr
    }

    private fun reset() {
        phoneNumberStr = ""
        phoneNumber.text = ""
    }

    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            R.id.backspace -> {
                reset()
                matchPattern()
                return true
            }
            R.id.button0 -> {
                phoneNumberStr += "+"
                return true
            }
        }
        return false
    }

    companion object {
        fun newInstance(uri: Uri?): DialerFragment {
            return DialerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_DATA, uri?.toString())
                }
            }
        }
    }
}
