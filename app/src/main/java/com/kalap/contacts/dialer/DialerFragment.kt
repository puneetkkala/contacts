package com.kalap.contacts.dialer

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.kalap.contacts.R
import com.kalap.contacts.common.*
import com.kalap.contacts.model.Contact
import com.kalap.contacts.views.ContactView
import com.kalap.contacts.views.DialerNumberView
import kotlinx.android.synthetic.main.dialer_fragment.*

private const val EXTRA_DATA = "data"

class DialerFragment : BaseFragment<DialerViewModel, ContactView, Contact>(R.layout.dialer_fragment, DialerViewModel::class, ContactView::class), View.OnClickListener {

    private var phoneNumberStr: String = ""

    private val dialerNumbersAdapter = BaseAdapter(DialerNumberView::class, object : BaseListener<Pair<String, String>> {
        override fun handleAction(action: String, model: Pair<String, String>) {
            when (action) {
                DialerNumberView.ACTION_NUMBER_CLICK -> {
                    phoneNumberStr += model.first
                    matchPattern()
                    phoneNumber.text = phoneNumberStr
                }
                DialerNumberView.ACTION_NUMBER_LONG_CLICK -> {
                    when (model.first) {
                        "0" -> phoneNumberStr += "+"
                    }
                }
            }
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonCall.setOnClickListener(this)
        backspace.setOnClickListener(this)
        backspace.setOnLongClickListener {
            reset()
            matchPattern()
            return@setOnLongClickListener true
        }
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
        dialerNumberRv.adapter = dialerNumbersAdapter
        viewModel.displayContactsLd.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
        viewModel.dialerNumbersLd.observe(viewLifecycleOwner) { items -> dialerNumbersAdapter.updateItems(items) }
        viewModel.loadDialer()
    }

    private fun matchPattern() {
        viewModel.getT9Contacts(phoneNumberStr)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonCall -> {
                if (phoneNumberStr.isNotEmpty()) {
                    context?.call(phoneNumberStr)
                }
            }
            R.id.backspace -> {
                if (phoneNumberStr.isNotEmpty()) {
                    phoneNumberStr = phoneNumberStr.substring(0, phoneNumberStr.length - 1)
                }
            }
        }
        matchPattern()
        phoneNumber.text = phoneNumberStr
    }

    private fun reset() {
        phoneNumberStr = ""
        phoneNumber.text = ""
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
