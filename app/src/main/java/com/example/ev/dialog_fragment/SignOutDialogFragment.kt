package com.example.ev.dialog_fragment
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


interface OnSignOutCallback{
    fun signOut()
}
class SignOutDialogFragment(val callback: OnSignOutCallback) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Sign Out") { _, _ ->
                callback.signOut()
                dismiss()
            }
            .setNegativeButton("Cancel", null)
        return builder.create()
    }
}