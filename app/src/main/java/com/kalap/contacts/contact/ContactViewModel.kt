package com.kalap.contacts.contact

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import com.kalap.contacts.model.Contact
import com.kalap.contacts.common.BaseViewModel
import com.kalap.contacts.common.getContrastColorForWhite
import com.kalap.contacts.common.initial
import com.kalap.contacts.database.ContactsDatabaseHelper
import io.realm.RealmList
import java.util.*
import kotlin.collections.ArrayList

class ContactViewModel(application: Application) : BaseViewModel(application) {

    val contactsLd = MutableLiveData<List<Contact>>()

    fun loadContacts() = runOnBackgroundThread({ load() })

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
                    contactHashMap[name] = contact
                } else {
                    contact = Contact()
                    contact.id = id
                    contact.initial = name.initial()
                    contact.color = getContrastColorForWhite()
                    contact.name = name
                    phNumList = RealmList()
                    phNumList.add(phoneNum)
                    contact.phoneNumberList = phNumList
                    contactHashMap[name] = contact
                }
            }
            cursor1.close()
        }
        val helper = ContactsDatabaseHelper()
        val contactList = ArrayList<Contact>()
        contactHashMap.forEach { contactList.add(it.value) }
        contactList.sortBy { it.name }
        helper.addContactBulk(contactList)
        contactsLd.postValue(contactList)
    }

    fun searchContacts(query: String) = runOnBackgroundThread({ search(query) })

    private fun search(query: String) {
        val helper = ContactsDatabaseHelper()
        if (query.isEmpty()) {
            contactsLd.postValue(helper.getAllContacts())
        } else {
            contactsLd.postValue(helper.search(query))
        }
    }
}
