package com.kalap.contacts.executors

import android.content.Context
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.common.BaseExecutor
import com.kalap.contacts.common.Common.log
import java.util.*

class T9Executor(context: Context): BaseExecutor(context) {
    lateinit var listener: T9SearchCompleteListener

    fun getT9Contacts(allContacts: ArrayList<Contact>?, phoneNumberStr: String) {
        val runnable = Runnable { load(allContacts, phoneNumberStr) }
        runOnBackgroundThread(runnable)
    }

    private fun load(allContacts: ArrayList<Contact>?, phoneNumberStr: String) {
        if (allContacts == null) return
        val t9Pattern = phoneNumberStr
                .replace("1", "")
                .replace("2", "[abc]")
                .replace("3", "[def]")
                .replace("4", "[ghi]")
                .replace("5", "[jkl]")
                .replace("6", "[mno]")
                .replace("7", "[pqrs]")
                .replace("8", "[tuv]")
                .replace("9", "[wxyz]")
                .replace("\\*", "")
                .replace("0", "")
                .replace("\\+", "")
                .replace("#", "") + ".*"
        val displayContacts = TreeMap<String, Contact>()
        val regex = Regex(t9Pattern)
        for (i in 0 until allContacts.size) {
            try {
                val contact = allContacts[i]
                if (regex.matches(contact.name.toLowerCase())) {
                    displayContacts[contact.name] = contact
                }
                if (displayContacts.size == 10) {
                    break
                }
            } catch (e: Exception) {
                e.log()
            }
        }

        listener.t9SearchCompleted(displayContacts)
    }

    interface T9SearchCompleteListener {
        fun t9SearchCompleted(displayContacts: TreeMap<String, Contact>)
    }
}
