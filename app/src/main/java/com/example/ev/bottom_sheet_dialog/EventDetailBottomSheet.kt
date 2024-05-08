package com.example.ev.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ev.R
import com.example.ev.data.Events
import com.example.ev.databinding.BottomSheetEventDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EventDetailBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetEventDetailBinding

    companion object {
        fun newInstance(event: Events): EventDetailBottomSheet {
            val args = Bundle().apply {
                putSerializable("event", event)
            }
            return EventDetailBottomSheet().also {
                it.arguments = args
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = BottomSheetEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getSerializable("event")?.let{
            val event = it as Events
            binding.categoryDesc.text = event.category
            binding.typeDesc.text = event.type
            binding.dateDesc.text = event.date
            binding.startTimeDesc.text = event.startTime
            binding.endTimeDesc.text = event.endTime
            binding.entranceFeeDesc.text = event.entrancefee
            binding.descriptionDesc.text = event.description
        }

        binding.back.setOnClickListener {
            dismiss()
        }
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

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}