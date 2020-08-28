package com.kalap.contacts.database

import com.kalap.contacts.model.Contact
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where

class ContactsDatabaseHelper {

    private val realm = Realm.getDefaultInstance()

    fun getAllContacts(): ArrayList<Contact> {
        val allContacts: ArrayList<Contact> = ArrayList()
        val realmResults = realm.where<Contact>().findAllSorted("name",Sort.ASCENDING)
        realmResults.forEach { allContacts.add(realm.copyFromRealm(it)) }
        return allContacts
    }

    fun search(query: String): ArrayList<Contact> {
        val allContacts: ArrayList<Contact> = ArrayList()
        val realmResults = if (query.isEmpty()) {
            realm.where<Contact>().findAll()
        } else {
            realm.where<Contact>().findAll().filter {
                (it.phoneNumberList.any { query.contains(it) || it.contains(query) }) ||
                        (it.name.contains(query) || query.contains(it.name))
            }
        }
        realmResults.forEach { allContacts.add(realm.copyFromRealm(it)) }
        return allContacts
    }

    fun getContactName(phoneNumber: String): String {
        realm.where<Contact>().findAll().forEach {
            if(it.phoneNumberList.any { phoneNumber.contains(it) || it.contains(phoneNumber) }) {
                return it.name
            }
        }
        return ""
    }

    fun addContactBulk(contactList: ArrayList<Contact>) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(contactList)
        }
    }
}
