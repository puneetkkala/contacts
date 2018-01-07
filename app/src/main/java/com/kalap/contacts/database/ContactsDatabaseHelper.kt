package com.kalap.contacts.database

import com.kalap.contacts.`object`.Contact
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where

class ContactsDatabaseHelper {

    private val realm = Realm.getDefaultInstance()

    fun getAllContacts(): ArrayList<Contact> {
        val allContacts: ArrayList<Contact> = ArrayList()
        val realmResults = realm.where<Contact>().findAllSorted("name",Sort.ASCENDING)
        realmResults.forEach { allContacts.add(it) }
        return allContacts
    }

    fun getContactName(phoneNumber: String): String {
        realm.where<Contact>().findAll().forEach {
            if(it.phoneNumberList.filter { phoneNumber.contains(it) || it.contains(phoneNumber) }.isNotEmpty()) {
                return it.name
            }
        }
        return ""
    }

    fun addContact(contact: Contact) {
        realm.executeTransactionAsync {
            it.insertOrUpdate(contact)
        }
    }

    fun addContactBulk(contactList: ArrayList<Contact>) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(contactList)
        }
    }
}
