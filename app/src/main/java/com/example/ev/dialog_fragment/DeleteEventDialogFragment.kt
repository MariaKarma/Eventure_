package com.example.ev.dialog_fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

interface OnDeleteEventCallback{
    fun deleteEvent()
}
class DeleteEventDialogFragment(val callback: OnDeleteEventCallback) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Delete Event")
            .setMessage("Are you sure you want to delete event?")
            .setPositiveButton("Delete") { _, _ ->
                callback.deleteEvent()
                dismiss()
            }
            .setNegativeButton("Cancel", null)
        return builder.create()
    }
}