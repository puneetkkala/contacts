package com.kalap.contacts.views

import com.google.android.material.snackbar.Snackbar
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
        itemView.contact_name.setOnClickListener {
            if (itemView.phone_number_list.visibility == View.VISIBLE) {
                itemView.phone_number_list.visibility = View.GONE
            } else {
                itemView.phone_number_list.visibility = View.VISIBLE
                if (model.phoneNumberList.isNotEmpty()) {
                    val phoneNumberAdapter = BaseAdapter(PhoneNumberView::class)
                    phoneNumberAdapter.updateItems(model.phoneNumberList)
                    itemView.phone_number_list.adapter = phoneNumberAdapter
                    itemView.phone_number_list.layoutManager = LinearLayoutManager(context)
                } else {
                    Snackbar.make(itemView.phone_number_list, "Phone number not available for this contact", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}