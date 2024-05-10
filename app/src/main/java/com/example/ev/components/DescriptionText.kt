package com.example.ev.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.ev.R
import com.example.ev.bottom_sheet_dialog.CountryBottomSheet
import com.example.ev.data.Country
import com.example.ev.databinding.SampleDescriptionTextBinding
import com.example.ev.interfaces.CountryCallBack
import com.example.ev.utils.AppController
import com.example.ev.utils.TextDrawable

import java.util.Locale


class DescriptionText : RelativeLayout {

    lateinit var binding: SampleDescriptionTextBinding
    private var inputType = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet?) {
        var icon: Drawable? = null
        var inputHint: String? = ""


        context.theme.obtainStyledAttributes(
            attrs, R.styleable.DescriptionText, 0, 0
        ).apply {

            try {

                inputType = getInt(R.styleable.DescriptionText_inputType, inputType)
                inputHint = getString(R.styleable.DescriptionText_inputHint)
                icon = getDrawable(R.styleable.DescriptionText_icon)
                Log.d("DescriptionText", "Attributes loaded")
            } finally {
                recycle()
            }
        }

        binding = SampleDescriptionTextBinding.inflate(LayoutInflater.from(context), this, true)

        Log.d("DescriptionText", "View binding inflated")
        binding.textInputLayout.startIconDrawable = icon
        binding.textInputLayout.hint = inputHint
        binding.textInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED))
        when (inputType) {
            0 -> {
                binding.text1.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }

            1 -> {
                binding.text1.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            2 -> {
                bindCountryPrefix("LB")
                binding.text1.inputType = InputType.TYPE_CLASS_PHONE
                binding.textInputLayout.setStartIconOnClickListener {
                    val fragmentManager = getFragmentManager(context)
                    if (fragmentManager != null) {
                        CountryBottomSheet(object : CountryCallBack {
                            override fun onCountrySelected(country: Country) {
                                bindCountryPrefix(country.code)
                            }
                        }).show(
                            fragmentManager, "CountryBottomSheet"
                        )
                    }
                }
            }
        }
    }

    fun showErrorIfNotValid(isNotValid: Boolean, errorMessage: String) {
        if (isNotValid) {
            binding.textInputLayout.error = errorMessage
        } else {
            binding.textInputLayout.error = ""
        }
    }


    fun setStateEnabled(enabled: Boolean) {
        binding.text1.isEnabled = enabled
        binding.textInputLayout.isEnabled = enabled
    }

    private fun getFragmentManager(context: Context): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getFragmentManager(context.baseContext)
            else -> null
        }
    }

    fun getEnteredText(): String {
        if (inputType == 2) {
            return binding.textInputLayout.prefixText.toString() + getTextWithoutPrefix()
        }
        return getTextWithoutPrefix()
    }

    fun getTextWithoutPrefix(): String {
        return binding.text1.text?.toString() ?: ""
    }


    fun getPrefix(): String {
        return binding.textInputLayout.prefixText.toString()
    }

    fun bindCountryPrefix(countryCode: String) {
        val country = AppController.instance.getCountryByCountryCode(countryCode)
        binding.textInputLayout.let {
            it.prefixText = country?.dialcode
            val countryFlag = "flags/" + country?.code?.lowercase(Locale.ROOT) + ".png"
            var flagDrawable: Drawable?
            try {
                val inputStream = context.assets.open(countryFlag)
                flagDrawable = Drawable.createFromStream(inputStream, null)
                val bitmap = (flagDrawable as BitmapDrawable).bitmap
                flagDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 100, 70, true))

            } catch (e: Exception) {
                flagDrawable = TextDrawable(country?.code ?: "")
            }

            it.startIconDrawable = flagDrawable
        }
    }

    fun setFieldBorderColor(isEmpty: Boolean) {
        if (isEmpty) {
            binding.textInputLayout.error = "Field cannot be empty "
        }

        else {
            binding.textInputLayout.error = null
        }
    }
    fun setFieldBorderColorEmail(isEmailNotValid: Boolean) {
        if (isEmailNotValid) {
            binding.textInputLayout.error = "Email is not valid"
        } else {
            binding.textInputLayout.error = null
        }
    }
    fun setFieldBorderColorPhone(isPhoneNotValid: Boolean) {
        if (isPhoneNotValid) {
            binding.textInputLayout.error = "Phone is not valid"
        } else {
            binding.textInputLayout.error = null
        }
    }

    fun setOnChangeListener(listener: (String) -> Unit) {
        binding.text1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing needed here
            }

            override fun afterTextChanged(editable: Editable?) {
                listener(editable.toString())
            }
        })
    }

    fun setMaxCharacters(maxCharacters: Int) {
        val filters = binding.text1.filters.toMutableList()
        val lengthFilter = InputFilter.LengthFilter(maxCharacters)
        val index = filters.indexOfFirst { it is InputFilter.LengthFilter }
        if (index != -1) {
            filters[index] = lengthFilter
        } else {
            filters.add(lengthFilter)
        }
        binding.text1.filters = filters.toTypedArray()
    }
    fun setText(text: String) {
        binding.text1.setText(text)
    }
}
