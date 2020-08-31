package com.kalap.contacts.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes

fun View.keepKeyboardHidden() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_HIDDEN)
}

fun Context.call(phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    callIntent.data = Uri.parse("tel:$phoneNumber")
    startActivity(callIntent)
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