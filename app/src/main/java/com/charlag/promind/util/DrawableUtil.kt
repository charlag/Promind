package com.charlag.promind.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * Created by charlag on 12/06/2017.
 */

fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) return this.bitmap
    val bitmap = android.graphics.Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight,
            android.graphics.Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}