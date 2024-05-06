package com.example.ev.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ev.R
import com.example.ev.data.Profile
import com.example.ev.databinding.SampleEditProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditProfileBottomSheet(
    private val isDraggable: Boolean = true,
    private val onCompletionListener: OnCompletionListener? = null
) : BottomSheetDialogFragment() {
    private lateinit var binding: SampleEditProfileBinding


    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isDraggable = isDraggable

    }

    interface OnCompletionListener {
        fun onDone(profile: Profile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = SampleEditProfileBinding.inflate(inflater, container, false)
        binding.save.bind("Save"){}
        binding.cancel.bind("Cancel"){}

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->
                val behavior =
                    com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behavior.state =
                    com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
            }
    }

    override fun getTheme(): Int = R.style.ABottomCardSheet

}