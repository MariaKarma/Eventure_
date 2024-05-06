package com.example.ev.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.ev.activities.LoginPage
import com.example.ev.bottom_sheet_dialog.EditProfileBottomSheet
import com.example.ev.data.Profile
import com.example.ev.databinding.FragmentProfileBinding
import com.example.ev.dialog_fragment.OnSignOutCallback
import com.example.ev.dialog_fragment.SignOutDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.logout.setOnClickListener {
            val signOutDialog = SignOutDialogFragment(object : OnSignOutCallback {
                override fun signOut() {
                    val intent = Intent(requireContext(), LoginPage::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            })
            signOutDialog.show(childFragmentManager, "sign_out_dialog")
        }

        binding.editProfile.setOnClickListener {


            EditProfileBottomSheet(onCompletionListener = object :
                EditProfileBottomSheet.OnCompletionListener {
                override fun onDone(profile: Profile) {
//                    updateProfile(profile)
                }
            }).show(
                (context as FragmentActivity).supportFragmentManager, "EditProfile"
            )
        }

        return binding.root
    }
}