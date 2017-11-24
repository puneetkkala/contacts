package com.kalap.contacts.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.`object`.PhoneLog
import com.kalap.contacts.views.CallLogView
import java.util.*


class CallLogAdapter(private val context: Context, private val phoneLogs: ArrayList<PhoneLog>) : RecyclerView.Adapter<CallLogView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogView {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.call_log_row, parent, false)
        return CallLogView(context,itemView)
    }

    override fun onBindViewHolder(holder: CallLogView, position: Int) {
        if (position > -1 && position < itemCount) holder.bindData(phoneLogs[position])
    }

    override fun getItemCount(): Int = phoneLogs.size
}