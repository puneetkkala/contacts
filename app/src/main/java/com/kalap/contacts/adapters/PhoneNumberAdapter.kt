package com.kalap.contacts.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.views.PhoneNumberView
import io.realm.RealmList

class PhoneNumberAdapter(private val activity: Activity, private val phoneNumbers: RealmList<String>) : RecyclerView.Adapter<PhoneNumberView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneNumberView {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_phone_num_row, parent, false)
        return PhoneNumberView(activity, itemView)
    }

    override fun onBindViewHolder(holder: PhoneNumberView, position: Int) {
        if (position > -1 && position < itemCount) holder.bindData(phoneNumbers[position]!!)
    }

    override fun getItemCount(): Int = phoneNumbers.size
}
