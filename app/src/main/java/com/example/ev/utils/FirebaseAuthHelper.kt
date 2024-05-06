package com.example.ev.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider


fun determineAuthProvider(): String? {
    val user = FirebaseAuth.getInstance().currentUser
    user?.providerData?.forEach {
        if (it.providerId.contains(GoogleAuthProvider.PROVIDER_ID)) {
            return "GOOGLE"
        } else if (it.providerId.contains(PhoneAuthProvider.PROVIDER_ID)) {
            return "PHONE"
        }
    }
    return null
}