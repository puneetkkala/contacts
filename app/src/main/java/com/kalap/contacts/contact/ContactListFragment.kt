package com.kalap.contacts.contact

import android.os.Bundle
import android.view.View
import com.kalap.contacts.R
import com.kalap.contacts.model.Contact
import com.kalap.contacts.common.BaseFragment
import com.kalap.contacts.common.onTextChange
import com.kalap.contacts.views.ContactView
import kotlinx.android.synthetic.main.contact_list_fragment.*

class ContactListFragment : BaseFragment<ContactViewModel, ContactView, Contact>(R.layout.contact_list_fragment, ContactViewModel::class, ContactView::class) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchEt.onTextChange { query -> viewModel.searchContacts(query)  }
        contactsRv.adapter = adapter
        viewModel.loadContacts()
        viewModel.contactsLd.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
    }
}
