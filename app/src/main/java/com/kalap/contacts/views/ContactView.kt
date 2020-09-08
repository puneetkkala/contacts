package com.kalap.contacts.views

import android.content.res.ColorStateList
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.model.Contact
import com.kalap.contacts.common.BaseAdapter
import com.kalap.contacts.common.BaseViewHolder
import kotlinx.android.synthetic.main.custom_contact_row.view.*

class ContactView(parent: ViewGroup) : BaseViewHolder<Contact>(parent, R.layout.custom_contact_row) {

    override fun bindTo(model: Contact) {
        itemView.contact_name.text = model.name
        itemView.initialsImage.text = model.initial
        itemView.initialsImage.backgroundTintList = ColorStateList.valueOf(model.color)
        handleExpandCollapse(model)
        itemView.contact_name.setOnClickListener {
            model.isExpanded = !model.isExpanded
            handleExpandCollapse(model)
        }
    }

    private fun handleExpandCollapse(model: Contact) {
        if (model.isExpanded && model.phoneNumberList.isNotEmpty()) {
            itemView.phone_number_list.visibility = View.VISIBLE
            val phoneNumberAdapter = BaseAdapter(PhoneNumberView::class)
            phoneNumberAdapter.updateItems(model.phoneNumberList)
            itemView.phone_number_list.adapter = phoneNumberAdapter
            itemView.phone_number_list.layoutManager = LinearLayoutManager(context)
        } else {
            itemView.phone_number_list.visibility = View.GONE
        }
    }
}