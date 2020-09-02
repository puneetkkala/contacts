package com.kalap.contacts.calllogs

import android.os.Bundle
import android.view.View
import com.kalap.contacts.R
import com.kalap.contacts.model.PhoneLog
import com.kalap.contacts.common.BaseFragment
import kotlinx.android.synthetic.main.call_logs_fragment.*

class CallLogsFragment : BaseFragment<CallLogViewModel, CallLogViewHolder, PhoneLog>(R.layout.call_logs_fragment, CallLogViewModel::class, CallLogViewHolder::class) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callLogs.adapter = adapter
        viewModel.phoneLogsLd.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
        viewModel.loadCallLogs()
    }
}
