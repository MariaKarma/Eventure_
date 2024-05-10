package com.example.ev.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.ev.R
import com.example.ev.data.Events
import com.example.ev.database.AppDatabase
import com.example.ev.database.Event
import com.example.ev.databinding.BottomSheetAddEventBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class AddEventBottomSheet(
    private val isDraggable: Boolean = true,
    private val onCompletionListener: AddEventBottomSheet.OnCompletionListener? = null
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetAddEventBinding
    private lateinit var database: AppDatabase
    private lateinit var dao: com.example.ev.database.Dao

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

        binding.add.bind("Add"){
            if (binding.add.isEnabled) {
                val event = createEventFromInput()
                addEventToDatabase(event)
                dismiss()
            }
        }
        setupDatabase()
        binding.cancel.bind("Cancel"){ dismiss()}

        setupFieldValidations()
        setupCategorySpinner()

        binding.eventName.setMaxCharacters(26)
        binding.type.setMaxCharacters(26)
        binding.latitude.setMaxCharacters(7)
        binding.longitude.setMaxCharacters(7)
        binding.date.setMaxCharacters(10)
        binding.startTime.setMaxCharacters(5)
        binding.endTime.setMaxCharacters(5)
        binding.fee.setMaxCharacters(4)
        binding.description.setMaxCharacters(470)



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
    private fun setupDatabase() {
        database = AppDatabase.getDatabase(requireContext())
        dao = database.Dao()
    }

    private fun addEventToDatabase(event: Event) {
        lifecycleScope.launch(Dispatchers.IO) {
            dao.insertEvent(event)
        }
    }

    private fun createEventFromInput(): Event {
        return Event(
            eventId = UUID.randomUUID().toString(),
            latitude = binding.latitude.getEnteredText(),
            longitude = binding.longitude.getEnteredText(),
            name = binding.eventName.getEnteredText(),
            category = binding.category.selectedItem.toString(),
            type = binding.type.getEnteredText(),
            date = binding.date.getEnteredText(),
            startTime = binding.startTime.getEnteredText(),
            endTime = binding.endTime.getEnteredText(),
            entrancefee = binding.fee.getEnteredText(),
            description = binding.description.getEnteredText()
        )
    }


    private fun setupFieldValidations() {
        binding.eventName.setOnChangeListener { validateFields() }
        binding.latitude.setOnChangeListener { validateFields() }
        binding.longitude.setOnChangeListener { validateFields() }
        binding.date.setOnChangeListener { validateFields() }
        binding.startTime.setOnChangeListener { validateFields() }
        binding.endTime.setOnChangeListener { validateFields() }
        binding.fee.setOnChangeListener { validateFields() }
    }

    private fun validateFields() {
        val isEventNameValid = binding.eventName.getEnteredText().isNotEmpty()
        val isLatitudeValid = binding.latitude.getEnteredText().toDoubleOrNull()?.let { it in -90.0..90.0 } == true
        val isLongitudeValid = binding.longitude.getEnteredText().toDoubleOrNull()?.let { it in -180.0..180.0 } == true
        val isTypeValid = binding.type.getEnteredText().isNotEmpty()
        val isDateValid = binding.date.getEnteredText().matches("\\d{4}-\\d{2}-\\d{2}".toRegex())
        val isStartTimeValid = binding.startTime.getEnteredText().matches("\\d{2}:\\d{2}".toRegex())
        val isEndTimeValid = binding.endTime.getEnteredText().matches("\\d{2}:\\d{2}".toRegex())
        val isFeeValid = binding.fee.getEnteredText().matches("\\d+\\$$".toRegex())
        val isDescriptionValid = binding.description.getEnteredText().isNotEmpty()
        val isCategoryValid = binding.category.selectedItemPosition >= 0

        val allValid = isEventNameValid && isLatitudeValid && isLongitudeValid &&
                isCategoryValid && isDateValid && isStartTimeValid &&
                isEndTimeValid && isFeeValid && isDescriptionValid && isTypeValid

        binding.add.isEnabled = allValid
    }

    private var userHasInteracted = false

    private fun setupCategorySpinner() {
        val categories = resources.getStringArray(R.array.add_event_categories)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.category.adapter = adapter

        binding.category.setOnTouchListener { _, _ ->
            userHasInteracted = true
            false
        }

        binding.category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (userHasInteracted) {
                    validateFields()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                if (userHasInteracted) {
                    binding.add.isEnabled = false
                }
            }
        }

        binding.add.isEnabled = false
    }
    override fun getTheme(): Int = R.style.ABottomCardSheet

}