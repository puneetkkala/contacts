package com.kalap.contacts.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class Contact: RealmObject() {
    @PrimaryKey var id: String = ""
    @Index var initial: String = ""
    var color: Int = 0
    @Index var name: String = ""
    var phoneNumberList: RealmList<String> = RealmList()
    @Ignore var isExpanded: Boolean = false
}