package com.dum.effectivemobiletest

import android.util.Patterns

    fun String?.isValidEmail() = !this?.isEmpty()!! && Patterns.EMAIL_ADDRESS.matcher(this).matches()

