package com.kalap.contacts.common

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.graphics.ColorUtils
import androidx.viewpager.widget.ViewPager
import java.util.*

fun String.oneContainsOther(other: String): Boolean {
    return this.contains(other) || other.contains(this)
}

fun View.keepKeyboardHidden() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_HIDDEN)
}

fun Context.call(phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    callIntent.data = Uri.parse("tel:$phoneNumber")
    startActivity(callIntent)
}

fun Context.saveNumber(phoneNumber: String) {
    val saveIntent = Intent(Intent.ACTION_INSERT)
    saveIntent.type = ContactsContract.Contacts.CONTENT_TYPE
    saveIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
    startActivity(saveIntent)
}

fun Context.longToast(@StringRes stringRes: Int) {
    val  toast = Toast.makeText(this, stringRes, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun EditText.onTextChange(body: (query: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            body.invoke(s?.toString() ?: "")
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}

fun ViewPager.onPageSelected(body: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            body.invoke(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

    })
}

fun getContrastColorForWhite(): Int {
    val random = Random()
    var backgroundColor = Color.argb(255, random.nextInt() % 128, random.nextInt() % 128, random.nextInt() % 128)
    while (ColorUtils.calculateContrast(Color.WHITE, backgroundColor) < 3.5) {
        backgroundColor = Color.argb(255, random.nextInt() % 128, random.nextInt() % 128, random.nextInt() % 128)
    }
    return backgroundColor
}

fun String.initial(): String {
    return if (isNotBlank() && first().isLetter()) {
        first().toUpperCase().toString()
    } else {
        "#"
    }
}