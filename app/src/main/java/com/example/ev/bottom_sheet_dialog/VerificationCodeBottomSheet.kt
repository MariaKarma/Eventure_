package com.example.ev.bottom_sheet_dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.ev.R
import com.example.ev.databinding.BottomSheetVerificationCodeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VerificationCodeBottomSheet(val callback: Callback) : BottomSheetDialogFragment() {

    interface Callback {
        fun resendCode()
        fun verifyCode(verificationCode: String)
    }

    private lateinit var binding: BottomSheetVerificationCodeBinding

    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.mTextField.text = "Remaining " + millisUntilFinished / 1000 + "s "
            binding.question.visibility = View.INVISIBLE
        }

        override fun onFinish() {
            binding.mTextField.text = ""
            binding.question.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetVerificationCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer.start()
        val verifyButton = view.findViewById<Button>(R.id.verifyButton)
        verifyButton.setOnClickListener {
            val verificationCode = binding.editTextVerificationCode.text.toString()
            if (verificationCode.isNotEmpty()) {
                callback.verifyCode(verificationCode)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter the verification code", Toast.LENGTH_SHORT).show()
            }
        }
        binding.resend.setOnClickListener {
            callback.resendCode()
            dismiss()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
            val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
            behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        timer.cancel()
    }

    override fun getTheme(): Int = R.style.BBottomCardSheet
}
