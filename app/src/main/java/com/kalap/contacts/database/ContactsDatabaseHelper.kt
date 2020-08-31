package com.kalap.contacts.database

import com.kalap.contacts.common.oneContainsOther
import com.kalap.contacts.model.Contact
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where

class ContactsDatabaseHelper {

    private val realm = Realm.getDefaultInstance()

    fun getAllContacts(): ArrayList<Contact> {
        val allContacts: ArrayList<Contact> = ArrayList()
        val realmResults = realm.where<Contact>().sort("name", Sort.ASCENDING).findAll()
        realmResults.forEach { allContacts.add(realm.copyFromRealm(it)) }
        return allContacts
    }

    fun search(query: String): List<Contact> {
        val lowercaseQuery = query.toLowerCase()
        val allContacts: ArrayList<Contact> = ArrayList()
        val realmResults = if (query.isEmpty()) {
            realm.where<Contact>().findAll()
        } else {
            realm.where<Contact>().findAll().filter {
                it.phoneNumberList.any { lowercaseQuery.oneContainsOther(it.toLowerCase()) } ||
                        (it.name.toLowerCase().oneContainsOther(lowercaseQuery))
            }
        }
        realmResults.forEach { allContacts.add(realm.copyFromRealm(it)) }
        return allContacts.sortedBy { it.name }
    }

    fun getContactName(phoneNumber: String): String {
        realm.where<Contact>().findAll().forEach {
            if (it.phoneNumberList.any { phoneNumber.oneContainsOther(it) }) {
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
