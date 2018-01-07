package com.kalap.contacts.`object`

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Contact: RealmObject() {

    @PrimaryKey var id: String = ""
    var name: String = ""
    var phoneNumberList: RealmList<String> = RealmList()
}