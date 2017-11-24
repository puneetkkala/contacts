package com.kalap.contacts.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import com.kalap.contacts.common.Events
import kotlinx.android.synthetic.main.custom_phone_num_row.view.*

/**
 * Created by kalapuneet on 24-11-2017.
 */
class PhoneNumberView(private val activity: Activity, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var mPhoneNumber: String

    fun bindData(number: String) {
        mPhoneNumber = number
        itemView.contact_number.text = number
        itemView.contact_number.setOnClickListener(this)
        itemView.contact_icon.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val callIntent = Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        callIntent.data = Uri.parse("tel:" + mPhoneNumber)
        activity.startActivity(callIntent)
        Events.postCallEvent("ContactListFragment")
    }
}