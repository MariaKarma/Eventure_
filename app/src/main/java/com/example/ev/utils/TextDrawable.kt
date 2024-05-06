package com.example.ev.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable(private val text: String) : Drawable() {
    private val paint: Paint = Paint()

    init {
        paint.color = Color.WHITE
        paint.textSize = 36f // Increase the text size to 36 pixels (adjust as needed)
        paint.isAntiAlias = true
        paint.isFakeBoldText = true
        paint.setShadowLayer(6f, 0f, 0f, Color.BLACK)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.LEFT
    }

    override fun draw(canvas: Canvas) {
        // Get the bounds of the drawable
        val bounds = bounds

        // Set the background color (You can change the color or use a drawable resource here)
        paint.color = Color.GRAY // For example, setting the background color to blue

        // Draw the background rectangle
        canvas.drawRect(bounds, paint)

        // Set the color back to white for the text
        paint.color = Color.WHITE

        // Draw the text at the center of the drawable
        val textWidth = paint.measureText(text)
        val x = (bounds.width() - textWidth) / 2f
        val y = bounds.height() / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas.drawText(text, x, y, paint)

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
