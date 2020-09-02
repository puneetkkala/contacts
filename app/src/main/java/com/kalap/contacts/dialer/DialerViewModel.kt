package com.kalap.contacts.dialer

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kalap.contacts.model.Contact
import com.kalap.contacts.common.BaseViewModel
import com.kalap.contacts.database.ContactsDatabaseHelper

class DialerViewModel(application: Application): BaseViewModel(application) {

    val displayContactsLd = MutableLiveData<List<Contact>>()
    val dialerNumbersLd = MutableLiveData<List<Pair<String, String>>>()

    fun loadDialer() {
        runOnBackgroundThread({ loadDialerNumbers() })
    }

    private fun loadDialerNumbers() {
        val dialerNumbers = mutableListOf<Pair<String, String>>()
        dialerNumbers.add("1" to "")
        dialerNumbers.add("2" to "ABC")
        dialerNumbers.add("3" to "DEF")
        dialerNumbers.add("4" to "GHI")
        dialerNumbers.add("5" to "JKL")
        dialerNumbers.add("6" to "MNO")
        dialerNumbers.add("7" to "PQRS")
        dialerNumbers.add("8" to "TUV")
        dialerNumbers.add("9" to "WXYZ")
        dialerNumbers.add("*" to "")
        dialerNumbers.add("0" to "+")
        dialerNumbers.add("#" to "")
        dialerNumbersLd.postValue(dialerNumbers)
    }

    fun getT9Contacts(phoneNumberStr: String) {
        runOnBackgroundThread({ load(phoneNumberStr) })
    }

    private fun load(phoneNumberStr: String) {
        val allContacts = ContactsDatabaseHelper().getAllContacts()
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
        val regex = Regex(t9Pattern)
        val displayContacts = allContacts.filter {
            regex.matches(it.name.toLowerCase()) || it.phoneNumberList.any { it.contains(phoneNumberStr) || phoneNumberStr.contains(it) }
        }
        val size = displayContacts.size
        if (size > 0) {
            displayContactsLd.postValue(displayContacts.subList(0, minOf(size, 10)))
        } else {
            displayContactsLd.postValue(emptyList())
        }
    }
}
