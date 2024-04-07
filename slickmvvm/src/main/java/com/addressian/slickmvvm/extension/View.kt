package com.addressian.slickmvvm.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt

fun View.removeRippleEffect() {
    var drawable = this.background
    if (drawable is RippleDrawable) {
        drawable = drawable.findDrawableByLayerId(0)
        this.background = drawable
    }
}

fun View.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(left, top, right, bottom)
    this.layoutParams = layoutParams
}

fun View.getBitmap(
    w: Int = this.width,
    h: Int = this.height
): Bitmap {
    val bitmap = Bitmap.createBitmap(
        w, h, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

/**
 * 为控件设置一个新的Drawable实例，而不是直接修改现有的实例，
 * 确保控件在重新显示时不重用已经改变的背景色
 */
fun View.updateBackgroundColor(@ColorInt int: Int) {
    val drawable = when (background) {
        is ShapeDrawable -> {
            val shapeDrawable = ShapeDrawable((background as ShapeDrawable).shape)
            shapeDrawable.paint.color = int
            shapeDrawable
        }

        is GradientDrawable -> {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = (background as GradientDrawable).shape
            gradientDrawable.setColor(int)
            gradientDrawable
        }

        is ColorDrawable -> {
            ColorDrawable(int)
        }

        else -> null
    }
    background = drawable ?: return
}