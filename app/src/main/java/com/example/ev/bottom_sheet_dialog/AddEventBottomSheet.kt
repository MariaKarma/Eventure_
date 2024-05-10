package com.example.ev.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ev.R
import com.example.ev.data.Events
import com.example.ev.databinding.BottomSheetAddEventBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddEventBottomSheet(
    private val isDraggable: Boolean = true,
    private val onCompletionListener: AddEventBottomSheet.OnCompletionListener? = null
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetAddEventBinding

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isDraggable = isDraggable

    }
    interface OnCompletionListener {
        fun onDone(event: Events)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetAddEventBinding.inflate(inflater, container, false)
        binding.add.bind("Add"){}
        binding.cancel.bind("Cancel"){ dismiss()}

        setupFieldValidations()

        binding.eventName.setMaxCharacters(26)
        binding.latitude.setMaxCharacters(7)
        binding.longitude.setMaxCharacters(7)
        binding.category.setMaxCharacters(26)
        binding.date.setMaxCharacters(10)
        binding.startTime.setMaxCharacters(5)
        binding.endTime.setMaxCharacters(5)
        binding.fee.setMaxCharacters(4)



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

    private fun setupFieldValidations() {
        binding.eventName.setOnChangeListener { validateFields() }
        binding.latitude.setOnChangeListener { validateFields() }
        binding.longitude.setOnChangeListener { validateFields() }
        binding.category.setOnChangeListener { validateFields() }
        binding.date.setOnChangeListener { validateFields() }
        binding.startTime.setOnChangeListener { validateFields() }
        binding.endTime.setOnChangeListener { validateFields() }
        binding.fee.setOnChangeListener { validateFields() }

        validateFields()  // Initial validation check
    }
    private fun validateFields() {
        val isEventNameValid = binding.eventName.getEnteredText().isNotEmpty() // Simple non-empty validation
        val isLatitudeValid = binding.latitude.getEnteredText().toDoubleOrNull()?.let { it in -90.0..90.0 } == true
        val isLongitudeValid = binding.longitude.getEnteredText().toDoubleOrNull()?.let { it in -180.0..180.0 } == true
        val isCategoryValid = binding.category.getEnteredText().isNotEmpty()
        val isDateValid = binding.date.getEnteredText().matches("\\d{4}-\\d{2}-\\d{2}".toRegex()) // Regex for YYYY-MM-DD
        val isStartTimeValid = binding.startTime.getEnteredText().matches("\\d{2}:\\d{2}".toRegex()) // Regex for HH:MM
        val isEndTimeValid = binding.endTime.getEnteredText().matches("\\d{2}:\\d{2}".toRegex())
        val isFeeValid = binding.fee.getEnteredText().matches("\\d+\\$$".toRegex()) // Regex for numbers ending with $

        val allValid = isEventNameValid && isLatitudeValid && isLongitudeValid &&
                isCategoryValid && isDateValid && isStartTimeValid &&
                isEndTimeValid && isFeeValid

        binding.add.isEnabled = allValid
    }

    override fun getTheme(): Int = R.style.ABottomCardSheet

}