package com.example.ev.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebasePhoneAuthHelpers(private val activity: Activity, val callback: Callback) {

    interface Callback {
        fun onSuccess(firebaseUser: FirebaseUser)
        fun openVerifyCodeScreen(firebasePhoneVerificationFields: GetFirebasePhoneVerificationFields)
        fun onFailure(exception: Exception?, errorMessage: String?)
    }

    interface GetFirebasePhoneVerificationFields {
        fun getVerificationId(): String
        fun getToken(): PhoneAuthProvider.ForceResendingToken
    }

    private fun getFBPhoneAuthBuilder(phoneNumber: String): PhoneAuthOptions.Builder {
        Log.e("getFBPhoneAuthBuilder","getFBPhoneAuthBuilder")

        return PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(120, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(phoneAuthCallbacks)
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        Log.e("startPhoneNumberVerification","startPhoneNumberVerification")

        val options = getFBPhoneAuthBuilder(phoneNumber).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(phoneNumber: String, resendToken: PhoneAuthProvider.ForceResendingToken) {
        val options = getFBPhoneAuthBuilder(phoneNumber).setForceResendingToken(resendToken).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            val user = FirebaseAuth.getInstance().currentUser
            if (task.isSuccessful && user != null) {
                Log.e("signInWithPhoneAuthCredential","signInWithPhoneAuthCredential")
                callback.onSuccess(user)

            } else {
                Log.e("signInWithPhoneAuthCredentialELSE","signInWithPhoneAuthCredentialELSE")

                callback.onFailure(task.exception, task.exception?.message)
            }
        }
    }

    fun signInWithPhoneAuthCredential(verificationCode: String, firebasePhoneVerificationFields: GetFirebasePhoneVerificationFields) {
        Log.e("signInWithPhoneAuthCredentialVERIFICATION","signInWithPhoneAuthCredentialVERIFICATION")

        val credential = PhoneAuthProvider.getCredential(firebasePhoneVerificationFields.getVerificationId(), verificationCode)
        signInWithPhoneAuthCredential(credential)
    }

    private val phoneAuthCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.e("onVerificationCompleted","onVerificationCompleted")

            signInWithPhoneAuthCredential(credential)
        }


        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("onVerificationFailed","onVerificationFailed")

            callback.onFailure(e, e.message)
        }

        override fun onCodeSent(
            verificationId: String, token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.e("onCodeSent","onCodeSent")

            callback.openVerifyCodeScreen(object : GetFirebasePhoneVerificationFields {
                override fun getVerificationId(): String {
                    Log.e("getVerificationId","getVerificationId")

                    return verificationId
                }
                override fun getToken(): PhoneAuthProvider.ForceResendingToken {
                    return token
                }
            })
        }
    }


}