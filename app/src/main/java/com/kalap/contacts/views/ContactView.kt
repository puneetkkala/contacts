package com.kalap.contacts.views

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.adapters.PhoneNumberAdapter
import kotlinx.android.synthetic.main.custom_contact_row.view.*

/**
 * Created by kalapuneet on 24-11-2017.
 */
class ContactView(private val activity: Activity, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var mContact: Contact

    fun bindData(contact: Contact) {
        mContact = contact
        itemView.contact_name.text = contact.name
        itemView.contact_name.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (itemView.phone_number_list.visibility == View.VISIBLE) {
            itemView.phone_number_list.visibility = View.GONE
        } else {
            itemView.phone_number_list.visibility = View.VISIBLE
            if (mContact.phoneNumberList.isNotEmpty()) {
                val phoneNumberAdapter = PhoneNumberAdapter(activity, mContact.phoneNumberList)
                itemView.phone_number_list.adapter = phoneNumberAdapter
                itemView.phone_number_list.layoutManager = LinearLayoutManager(activity)
            } else {
                Snackbar.make(itemView.phone_number_list, "Phone number not available for this contact", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}