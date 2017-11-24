package com.kalap.contacts.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.views.ContactView
import java.util.*

class ContactAdapter(private val activity: Activity, private var contactTreeMap: TreeMap<String, Contact>) : RecyclerView.Adapter<ContactView>() {
    private var contactNames: ArrayList<String>

    init {
        this.contactNames = ArrayList(contactTreeMap.keys)
    }

    fun updateData(contactTreeMap: TreeMap<String, Contact>) {
        this.contactTreeMap = contactTreeMap
        this.contactNames = ArrayList(contactTreeMap.keys)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactView {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_contact_row, parent, false)
        return ContactView(activity, itemView)
    }

    override fun onBindViewHolder(holder: ContactView, position: Int) {
        if (position > -1 && position < itemCount) holder.bindData(contactTreeMap[contactNames[position]]!!)
    }

    override fun getItemCount(): Int = contactTreeMap.size
}
