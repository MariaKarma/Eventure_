package com.example.ev.firebase


import com.example.ev.database.User


import android.content.Context
import com.example.ev.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UpdateUserDataHelpers(
    val callback: Callback,
    val context: Context,
    private val userViewModel: UserViewModel,
) {

    interface Callback {
        fun navigateToNextScreen(isNewUser: Boolean)
        fun onFailure(exception: Exception?, errorMessage: String?)
    }


    private fun insertUserInDB(user: User, isNewUser: Boolean) {
        userViewModel.insertUser(user)

    }

    fun createUserIfNotExists(user: User) {
        val uid = FirebaseAuth.getInstance().uid!!
        val dbRef = FirebaseDatabase.getInstance().getReference("User").child(uid)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getValue(User::class.java) != null) {

                    val userData = dataSnapshot.getValue(User::class.java)!!

                    insertUserInDB(userData,false)
                } else {
                    dbRef.setValue(user).addOnSuccessListener {

                        insertUserInDB(user, true)
                    }.addOnFailureListener { err ->

                        callback.onFailure(err, err.message)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

                callback.onFailure(databaseError.toException(), databaseError.message)
            }
        })
    }

}