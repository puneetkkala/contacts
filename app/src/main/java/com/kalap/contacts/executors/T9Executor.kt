package com.kalap.contacts.executors

import android.content.Context
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.common.BaseExecutor
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
                .replace("1".toRegex(), "")
                .replace("2".toRegex(), "[abc]")
                .replace("3".toRegex(), "[def]")
                .replace("4".toRegex(), "[ghi]")
                .replace("5".toRegex(), "[jkl]")
                .replace("6".toRegex(), "[mno]")
                .replace("7".toRegex(), "[pqrs]")
                .replace("8".toRegex(), "[tuv]")
                .replace("9".toRegex(), "[wxyz]")
                .replace("\\*".toRegex(), "")
                .replace("0".toRegex(), "")
                .replace("\\+".toRegex(), "")
                .replace("#".toRegex(), "") + ".*"
        val displayContacts = TreeMap<String, Contact>()
        allContacts
                .filter {it.name.toLowerCase().matches(t9Pattern.toRegex())}
                .takeLastWhile {displayContacts.size < 10}
                .forEach { displayContacts.put(it.name,it)}
        listener.t9SearchCompleted(displayContacts)
    }

    interface T9SearchCompleteListener {
        fun t9SearchCompleted(displayContacts: TreeMap<String, Contact>)
    }
}
