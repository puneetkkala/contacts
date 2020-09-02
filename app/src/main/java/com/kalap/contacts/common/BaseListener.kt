package com.kalap.contacts.common

interface BaseListener<T> {
    fun handleAction(action: String, model: T)
}