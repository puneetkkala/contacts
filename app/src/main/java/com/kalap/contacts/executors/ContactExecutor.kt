package com.kalap.contacts.executors

import android.content.Context
import android.provider.ContactsContract
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.common.BaseExecutor
import com.kalap.contacts.common.Common
import com.kalap.contacts.database.ContactsDatabaseHelper
import io.realm.RealmList
import java.util.*
import kotlin.collections.ArrayList

class ContactExecutor(val context: Context): BaseExecutor(context) {

    fun loadContacts() = runOnBackgroundThread(Runnable { load() })

    private fun load() {
        val contactHashMap = HashMap<String, Contact>()
        val contentResolver = context.contentResolver
        val cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC")
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                val contact: Contact
                val id = "" + cursor1.getInt(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNum = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phoneNum = phoneNum.replace("-".toRegex(), "").replace(" ".toRegex(), "")
                val phNumList: RealmList<String>
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
                    contact.id = id
                    contact.name = name
                    phNumList = RealmList()
                    phNumList.add(phoneNum)
                    contact.phoneNumberList = phNumList
                    contactHashMap.put(name, contact)
                }
            }
            cursor1.close()
        }
        val helper = ContactsDatabaseHelper()
        var contactList = ArrayList<Contact>()
        contactHashMap.forEach { contactList.add(it.value) }
        helper.addContactBulk(contactList)
        pref.putLong(Common.LAST_FETCH_TIME, System.currentTimeMillis())
    }
}
