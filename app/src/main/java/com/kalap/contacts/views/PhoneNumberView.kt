package com.kalap.contacts.views

import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.common.BaseViewHolder
import com.kalap.contacts.common.call
import kotlinx.android.synthetic.main.custom_phone_num_row.view.*

class PhoneNumberView(parent: ViewGroup) : BaseViewHolder<String>(parent, R.layout.custom_phone_num_row) {

    override fun bindTo(model: String) {
        itemView.contact_number.text = model
        itemView.contact_number.setOnClickListener { context.call(model) }
    }
}