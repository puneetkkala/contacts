package com.kalap.contacts.model

import androidx.annotation.ColorInt

data class PhoneLog (
    @ColorInt var color: Int,
    var initial: String,
    var name: String,
    var number: String,
    var type: String,
    var date: String,
    var duration: String
)
