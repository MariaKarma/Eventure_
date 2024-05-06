package com.example.ev.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ev.bottom_sheet_dialog.EditProfileBottomSheet
import com.example.ev.bottom_sheet_dialog.VerificationCodeBottomSheet
import com.example.ev.data.Profile
import com.example.ev.database.AppDatabase
import com.example.ev.databinding.LoginPageBinding
import com.example.ev.firebase.FirebasePhoneAuthHelpers
import com.example.ev.utils.determineAuthProvider
import com.example.ev.utils.isValidPhoneNumber
import com.example.ev.utils.splitDisplayName
import com.google.firebase.auth.FirebaseUser
import com.example.ev.database.User
import com.example.ev.database.userRole
import com.example.ev.firebase.FirebaseGoogleAuthHelpers
import com.example.ev.firebase.FirebaseGoogleAuthHelpers.Companion.FIREBASE_GOOGLE_SIGN_IN
import com.example.ev.firebase.UpdateUserDataHelpers
import com.example.ev.repository.UserRepository
import com.example.ev.viewmodel.UserViewModel
import com.example.ev.viewmodel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class LoginPage : AppCompatActivity(), FirebasePhoneAuthHelpers.Callback, FirebaseGoogleAuthHelpers.Callback, UpdateUserDataHelpers.Callback  {
    private lateinit var binding: LoginPageBinding
    var firebasePhoneAuthHelpers = FirebasePhoneAuthHelpers(this, this)
    private var firebaseGoogleAuthHelpers = FirebaseGoogleAuthHelpers()

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository(AppDatabase.getDatabase(this)))
        )[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skip.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.bind("Log In"){
            val isPhoneValid = isValidPhoneNumber(
                this,
                binding.phoneNumber.getTextWithoutPrefix(),
                Integer.parseInt(binding.phoneNumber.getPrefix())
            )
            binding.phoneNumber.showErrorIfNotValid(!isPhoneValid, "Invalid Phone Number")
            if (isPhoneValid) {
                binding.progressBar.visibility = View.VISIBLE
                firebasePhoneAuthHelpers.startPhoneNumberVerification(binding.phoneNumber.getEnteredText())
            }
        }
        binding.googlebutton.bind("Log In With Google"){
            binding.progressBar.visibility = View.VISIBLE
            firebaseGoogleAuthHelpers.signInWithGoogle(this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FIREBASE_GOOGLE_SIGN_IN) {
            firebaseGoogleAuthHelpers.handleResult(this, data, this)
        }
    }

    override fun onSuccess(firebaseUser: FirebaseUser) {

        val userEmail = firebaseUser.email ?: ""
        val displayName = firebaseUser.displayName ?: ""
        val uid = firebaseUser.uid
        val (firstName, lastName) = splitDisplayName(displayName)
        var phonePrefix = ""
        var phoneNum = ""

        if (determineAuthProvider() == "PHONE") {
            phonePrefix = binding.phoneNumber.getPrefix()
            phoneNum = binding.phoneNumber.getTextWithoutPrefix()
        }

        val user = User(
            userId = uid,
            firstName = firstName,
            lastName = lastName,
            email = userEmail,
            phoneNumber = phoneNum,
            phoneCountryCode = phonePrefix,
            role = userRole.MEMBER
        )

        UpdateUserDataHelpers(this, this, viewModel).
        createUserIfNotExists(user)

    }

    override fun openVerifyCodeScreen(firebasePhoneVerificationFields: FirebasePhoneAuthHelpers.GetFirebasePhoneVerificationFields) {
        binding.progressBar.visibility = View.GONE
        VerificationCodeBottomSheet(object : VerificationCodeBottomSheet.Callback {
            override fun resendCode() {
                binding.progressBar.visibility = View.VISIBLE
                firebasePhoneAuthHelpers.resendVerificationCode(binding.phoneNumber.getEnteredText(), firebasePhoneVerificationFields.getToken())
            }

            override fun verifyCode(verificationCode: String) {
                binding.progressBar.visibility = View.VISIBLE
                firebasePhoneAuthHelpers.signInWithPhoneAuthCredential(verificationCode, firebasePhoneVerificationFields)
            }
        }).show(supportFragmentManager, "VerificationCodeBottomSheet")
    }

    override fun navigateToNextScreen(isNewUser: Boolean) {
        binding.progressBar.visibility = View.GONE
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            viewModel.getUser().observe(this@LoginPage) { user ->
                Log.d("USER LOGIN", "$user")
                if (user != null) {
                    val isFirstNameMissing = user.firstName.isEmpty()
                    val isLastNameMissing = user.lastName.isEmpty()
                    if (isNewUser || isFirstNameMissing || isLastNameMissing) {
                        val bottomSheet = EditProfileBottomSheet(
                            false,
                            object : EditProfileBottomSheet.OnCompletionListener {
                                override fun onDone(profile: Profile) {

                                    val intent =
                                        Intent(this@LoginPage, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    } else {

                        val intent = Intent(this@LoginPage, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }


                } else {
                    println("User not found")
                }
            }
        } else {
            val intent = Intent(this@LoginPage, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onFailure(exception: Exception?, errorMessage: String?) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            this@LoginPage, "Failed: $errorMessage", Toast.LENGTH_LONG
        ).show()
    }
}