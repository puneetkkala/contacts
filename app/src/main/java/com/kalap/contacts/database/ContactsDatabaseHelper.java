package com.kalap.contacts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kalap.contacts.object.Contact;

import java.util.ArrayList;

public class ContactsDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactsDb";
    private static final String CONTACTS_TABLE = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE_NUM = "ph_num_list";

    public ContactsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + CONTACTS_TABLE + "(" +
                TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TAG_NAME + " TEXT," +
                TAG_PHONE_NUM + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + CONTACTS_TABLE;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    private String getCommaSeperatedList(ArrayList<String> phNumList) {
        String phNumComma = "";
        for(String phNum: phNumList) {
            phNumComma += phNum + ",";
        }
        return phNumComma.substring(0,phNumComma.length()-1);
    }

    public void addContact(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME,contact.getName());
        contentValues.put(TAG_PHONE_NUM,getCommaSeperatedList(contact.getPhoneNumberList()));
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CONTACTS_TABLE,null,contentValues);
        db.close();
    }

    private ArrayList<String> getArrayList(String phNumComma) {
        String phNumArray[] = phNumComma.split(",");
        ArrayList<String> phNumList = new ArrayList<>();
        for(String phNum:phNumArray) {
            phNumList.add(phNum);
        }
        return phNumList;
    }

    public ArrayList<Contact> getAllContacts() {
        String selectAll = "SELECT * FROM " + CONTACTS_TABLE + " ORDER BY " + TAG_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll,null);
        ArrayList<Contact> contacts = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setName(cursor.getString(1));
                contact.setPhoneNumberList(getArrayList(cursor.getString(2)));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contacts;
    }
}
