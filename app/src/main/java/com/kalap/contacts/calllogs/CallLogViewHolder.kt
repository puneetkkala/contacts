package com.kalap.contacts.calllogs

import android.graphics.Color
import android.text.TextUtils
import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.model.PhoneLog
import com.kalap.contacts.common.BaseViewHolder
import com.kalap.contacts.common.call
import kotlinx.android.synthetic.main.call_log_row.view.*

class CallLogViewHolder(parent: ViewGroup) : BaseViewHolder<PhoneLog>(parent, R.layout.call_log_row) {

    override fun bindTo(model: PhoneLog) {
        itemView.call_image.setOnClickListener{ context.call(model.number) }
        itemView.contact_name.text = if (TextUtils.isEmpty(model.name)) model.number else model.name
        itemView.type_date.text = when (model.type) {
            "Rejected","Missed","" -> model.type
            else -> model.type + ", " + model.duration
        }
        itemView.type_date.setTextColor(Color.parseColor(
                when(model.type) {
                    "Rejected","Missed" -> "#F44336"
                    else -> "#8B99A3"
                }
        ))
        itemView.duration.setTextColor(Color.parseColor("#8B99A3"))
        itemView.duration.text = model.date
    }
}