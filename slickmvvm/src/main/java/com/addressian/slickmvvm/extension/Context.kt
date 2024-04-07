package com.addressian.slickmvvm.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun Context.showToast(@StringRes messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(messageId), duration)
}

fun Context.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    Toast.makeText(this, message, duration).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}

/**
 * 动态判断当前窗口是否显示在大屏下
 */
fun Context.isTablet(): Boolean {
    return this.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

@ColorInt
fun Context.getThemeColor(attr: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

inline fun <reified T : Any> Context.intent() =
    Intent(this, T::class.java)

inline fun <reified T : Any> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

inline fun <reified T : Activity> Context?.startActivity() =
    this?.startActivity(Intent(this, T::class.java))

inline fun <reified T : Activity> Context?.startActivity(intentBody: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.intentBody()
    this?.startActivity(intent)
}