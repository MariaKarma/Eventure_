package com.example.ev.utils

import android.content.Context
import android.util.Patterns
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

fun isValidPhoneNumber(context: Context, phoneNumber: String, countryCode: Int): Boolean {


        val phoneNumberUtil = PhoneNumberUtil.createInstance(context)
    try {
        val parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, phoneNumberUtil.getRegionCodeForCountryCode(countryCode))
        return phoneNumberUtil.isValidNumber(parsedPhoneNumber)

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun splitDisplayName(displayName: String): Pair<String, String> {
    val names = displayName.split(" ")
    val firstName = names.getOrNull(0) ?: ""
    val lastName = names.subList(1, names.size).joinToString(" ")
    return firstName to lastName
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}