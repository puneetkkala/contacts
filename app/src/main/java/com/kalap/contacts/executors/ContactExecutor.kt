package com.kalap.contacts.executors

import android.content.Context
import android.os.Looper
import android.provider.ContactsContract
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.common.BaseExecutor
import com.kalap.contacts.database.ContactsDatabaseHelper
import java.util.*

class ContactExecutor: BaseExecutor() {

    fun loadContacts(context: Context) {
        val runnable = Runnable { load(context) }
        runButNotOnMainThread(runnable, Looper.getMainLooper().thread)
    }

    private fun load(context: Context) {
        val contactHashMap = HashMap<String, Contact>()
        val contentResolver = context.contentResolver
        val cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC")
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                val contact: Contact
                val name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNum = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phoneNum = phoneNum.replace("-".toRegex(), "").replace(" ".toRegex(), "")
                val phNumList: ArrayList<String>
                if (contactHashMap.containsKey(name)) {
                    contact = contactHashMap[name]!!
                    phNumList = contact.phoneNumberList
                    if (!phNumList.contains(phoneNum)) {
                        phNumList.add(phoneNum)
                    }
                    contact.phoneNumberList = phNumList
                    contactHashMap.put(name, contact)
                } else {
                    contact = Contact()
                    contact.name = name
                    phNumList = ArrayList()
                    phNumList.add(phoneNum)
                    contact.phoneNumberList = phNumList
                    contactHashMap.put(name, contact)
                }
            }
            cursor1.close()
        }
        for (name in contactHashMap.keys) {
            val helper = ContactsDatabaseHelper(context)
            helper.addContact(contactHashMap[name]!!)
        }
        val sharedPreferences1 = context.getSharedPreferences("contactsPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences1.edit()
        editor.putLong("lastFetchTime", System.currentTimeMillis())
        editor.apply()
    }
}
