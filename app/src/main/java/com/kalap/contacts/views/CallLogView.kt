package com.kalap.contacts.views

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.kalap.contacts.`object`.PhoneLog
import com.kalap.contacts.common.Events
import kotlinx.android.synthetic.main.call_log_row.view.*

class CallLogView(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(phoneLog: PhoneLog) {
        itemView.call_image.setOnClickListener{
            val callIntent = Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            callIntent.data = Uri.parse("tel:" + phoneLog.number)
            context.startActivity(callIntent)
            Events.postCallEvent("CallLogFragment")
        }
        itemView.contact_name.text = if (TextUtils.isEmpty(phoneLog.name)) phoneLog.number else phoneLog.name
        itemView.type_date.text = when (phoneLog.type) {
            "Rejected","Missed",null -> phoneLog.type
            else -> phoneLog.type + ", " + phoneLog.duration
        }
        itemView.type_date.setTextColor(Color.parseColor(
                when(phoneLog.type) {
                    "Rejected","Missed" -> "#F44336"
                    else -> "#8B99A3"
                }
        ))
        itemView.duration.setTextColor(Color.parseColor("#8B99A3"))
        itemView.duration.text = phoneLog.date
    }
}