package com.kalap.contacts.views

import android.view.ViewGroup
import com.kalap.contacts.R
import com.kalap.contacts.common.BaseViewHolder
import kotlinx.android.synthetic.main.item_dialer_number.view.*

class DialerNumberView(parent: ViewGroup) : BaseViewHolder<Pair<String, String>>(parent, R.layout.item_dialer_number) {

    override fun bindTo(model: Pair<String, String>) {
        itemView.titleTv.text = model.first
        itemView.subtitleTv.text = model.second
        itemView.rootView.setOnClickListener {
            listener?.handleAction(ACTION_NUMBER_CLICK, model)
        }
        itemView.rootView.setOnLongClickListener {
            listener?.handleAction(ACTION_NUMBER_LONG_CLICK, model)
            return@setOnLongClickListener true
        }
    }

    companion object {
        const val ACTION_NUMBER_CLICK = "DialerNumberView_actionNumberClick"
        const val ACTION_NUMBER_LONG_CLICK = "DialerNumberView_actionNumberLongClick"
    }
}