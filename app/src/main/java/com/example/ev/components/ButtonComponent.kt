package com.example.ev.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.ev.R

class ButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var buttonClickable = true

    init {

        var buttonText: String? = null

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonComponent,
            0, 0
        ).apply {

            try {
                buttonText = getString(R.styleable.ButtonComponent_buttonText)
                buttonClickable =
                    getBoolean(R.styleable.ButtonComponent_buttonComponentEnable, buttonClickable)
                val attrsArray = intArrayOf(android.R.attr.drawableStart)
                val typedArray = context.obtainStyledAttributes(attrs, attrsArray)
                val drawableStart = typedArray.getDrawable(0)
                drawableStart?.let {
                    if (it is BitmapDrawable) {
                        val bitmap = it.bitmap
                        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true)
                        val scaledDrawable = BitmapDrawable(resources, scaledBitmap)
                        setCompoundDrawablesWithIntrinsicBounds(scaledDrawable, null, null, null)
                    } else { }
                }
            } finally {
                recycle()
            }
        }
        updateButtonState(buttonClickable)
        text = buttonText
    }

    fun updateButtonState(buttonEnabled: Boolean) {
        isEnabled = buttonEnabled
        buttonClickable = buttonEnabled
        isClickable = buttonEnabled

    }

    fun bind(value: String, action: () -> Unit) {
        text = value
        setOnClickListener {
            action.invoke() }
    }
}