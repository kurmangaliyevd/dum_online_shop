package com.dum.effectivemobiletest

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager

fun String?.isValidEmail() = !this?.isEmpty()!! && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}
