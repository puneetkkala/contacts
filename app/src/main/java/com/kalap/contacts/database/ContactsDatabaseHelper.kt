package com.kalap.contacts.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.kalap.contacts.`object`.Contact

import java.util.ArrayList

class ContactsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allContacts: ArrayList<Contact>
        get() {
            val selectAll = "SELECT * FROM $CONTACTS_TABLE ORDER BY $TAG_NAME"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectAll, null)
            val contacts = ArrayList<Contact>()
            if (cursor.moveToFirst()) {
                do {
                    val contact = Contact()
                    contact.name = cursor.getString(1)
                    contact.phoneNumberList.addAll(getArrayList(cursor.getString(2)))
                    contacts.add(contact)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return contacts
        }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + CONTACTS_TABLE + "(" +
                TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TAG_NAME + " TEXT," +
                TAG_PHONE_NUM + " TEXT)"
        sqLiteDatabase.execSQL(createTable)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        val dropTable = "DROP TABLE IF EXISTS " + CONTACTS_TABLE
        sqLiteDatabase.execSQL(dropTable)
        onCreate(sqLiteDatabase)
    }

    private fun getCommaSeperatedList(phNumList: ArrayList<String>): String {
        var phNumComma = ""
        phNumList.forEach {phNumComma += it + ","}
        return phNumComma.substring(0, phNumComma.length - 1)
    }

    fun addContact(contact: Contact) {
        val contentValues = ContentValues()
        contentValues.put(TAG_NAME, contact.name)
        contentValues.put(TAG_PHONE_NUM, getCommaSeperatedList(contact.phoneNumberList))
        val db = this.writableDatabase
        db.insert(CONTACTS_TABLE, null, contentValues)
        db.close()
    }

    private fun getArrayList(phNumComma: String): List<String> = ArrayList<String>() + phNumComma.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ContactsDb"
        private val CONTACTS_TABLE = "contacts"
        private val TAG_ID = "id"
        private val TAG_NAME = "name"
        private val TAG_PHONE_NUM = "ph_num_list"
    }
}
