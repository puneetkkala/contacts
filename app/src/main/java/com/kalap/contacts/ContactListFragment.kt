package com.kalap.contacts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kalap.contacts.`object`.Contact
import com.kalap.contacts.adapters.ContactAdapter
import com.kalap.contacts.database.ContactsDatabaseHelper
import java.util.*
import kotlinx.android.synthetic.main.contact_list_fragment.*

class ContactListFragment : Fragment(), TextWatcher {

    private lateinit var displayAll: TreeMap<String, Contact>
    private lateinit var displayList: TreeMap<String, Contact>
    private lateinit var contactAdapter: ContactAdapter

    private fun prepareList() {
        val helper = ContactsDatabaseHelper(activity)
        val contacts = helper.allContacts
        displayAll = TreeMap()
        contacts.forEach {displayAll.put(it.name, it)}
        displayList = TreeMap(displayAll)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.contact_list_fragment, container, false)
        prepareList()
        dataChange()
        edit_query_text.addTextChangedListener(this)
        return view
    }

    private fun dataChange() {
        contactAdapter = ContactAdapter(activity, displayList)
        contacts_recycler_view.adapter = contactAdapter
        contacts_recycler_view.layoutManager = LinearLayoutManager(activity)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString().isEmpty()) {
            displayList = TreeMap(displayAll)
        } else {
            displayList = TreeMap()
            displayAll.keys
                    .filter { it.toLowerCase().startsWith(s.toString().toLowerCase()) }
                    .forEach { displayList.put(it, displayAll[it]!!) }
        }
        dataChange()
    }

    override fun afterTextChanged(s: Editable) {
    }

    companion object {
        fun newInstance(): ContactListFragment = ContactListFragment()
    }
}
