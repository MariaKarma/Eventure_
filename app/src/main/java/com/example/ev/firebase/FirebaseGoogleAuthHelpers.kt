package com.example.ev.firebase

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import com.example.ev.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseGoogleAuthHelpers {

    interface Callback {
        fun onSuccess(firebaseUser: FirebaseUser)
        fun onFailure(exception: Exception?, errorMessage: String?)
    }

    companion object {
        const val FIREBASE_GOOGLE_SIGN_IN = 9001
    }

    fun signInWithGoogle(activity: Activity) {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            activity.getString(
                R.string.default_web_client_id
            )
        ).requestEmail().requestProfile().build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        ActivityCompat.startActivityForResult(
            activity,
            googleSignInClient.signInIntent,
            FIREBASE_GOOGLE_SIGN_IN,
            null
        )
    }

    fun handleResult(activity: Activity, data: Intent?, callback: Callback) {

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {

            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity) { result ->

                    val user = FirebaseAuth.getInstance().currentUser

                    if (result.isSuccessful && user != null) {

                        callback.onSuccess(user)

                    } else {

                        callback.onFailure(result.exception, result.exception?.message)
                    }
                }

        } catch (e: Exception) {
            callback.onFailure(e, e.message)
        }
    }
}
