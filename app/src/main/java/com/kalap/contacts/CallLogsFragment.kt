package com.kalap.contacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kalap.contacts.adapters.CallLogAdapter
import com.kalap.contacts.listeners.CallLogLoadListener
import com.kalap.contacts.executors.CallLogExecutor
import com.kalap.contacts.`object`.PhoneLog

import java.util.ArrayList
import kotlinx.android.synthetic.main.call_logs_fragment.*

class CallLogsFragment : Fragment(), CallLogLoadListener {

    private var phoneLogs: ArrayList<PhoneLog>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.call_logs_fragment, container, false)
        phoneLogs = ArrayList()
        resetAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        val executor = CallLogExecutor()
        executor.listener = this
        executor.loadCallLogs(activity)
    }

    private fun resetAdapter() {
        activity.runOnUiThread {
            val callLogAdapter = CallLogAdapter(activity, phoneLogs!!)
            call_logs.adapter = callLogAdapter
            call_logs.layoutManager = LinearLayoutManager(activity)
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
                    val executor = CallLogExecutor()
                    executor.listener = this
                    executor.loadCallLogs(activity)
                }
            }
        }
    }

    companion object {
        fun newInstance(): CallLogsFragment = CallLogsFragment()
    }
}
