package com.kalap.contacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kalap.contacts.`object`.PhoneLog
import com.kalap.contacts.adapters.CallLogAdapter
import com.kalap.contacts.executors.CallLogExecutor
import com.kalap.contacts.listeners.CallLogLoadListener
import kotlinx.android.synthetic.main.call_logs_fragment.view.*
import java.util.*

class CallLogsFragment : Fragment(), CallLogLoadListener {

    private lateinit var phoneLogs: ArrayList<PhoneLog>
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.call_logs_fragment, container, false)!!
        phoneLogs = ArrayList()
        resetAdapter()
        return root
    }

    override fun onResume() {
        super.onResume()
        val executor = CallLogExecutor(activity)
        executor.listener = this
        executor.loadCallLogs()
    }

    private fun resetAdapter() {
        activity.runOnUiThread {
            val callLogAdapter = CallLogAdapter(activity, phoneLogs)
            root.call_logs.adapter = callLogAdapter
            root.call_logs.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCallLogLoaded(phoneLogs: ArrayList<PhoneLog>) {
        this.phoneLogs = phoneLogs
        resetAdapter()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            103 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val executor = CallLogExecutor(activity)
                    executor.listener = this
                    executor.loadCallLogs()
                }
            }
        }
    }

    companion object {
        fun newInstance(): CallLogsFragment = CallLogsFragment()
    }
}
