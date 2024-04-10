package com.addressian.slickmvvm.extension

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import com.google.android.material.tabs.TabLayout

fun TabLayout.setChildMargin(
    context: Context,
    @DimenRes top: Int,
    @DimenRes right: Int,
    @DimenRes bottom: Int,
    @DimenRes left: Int
) {
    val tabs = (getChildAt(0) as ViewGroup)
    for (i in 0 until tabs.childCount) {
        val tab = tabs.getChildAt(i)
        val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        layoutParams.topMargin = context.resources.getDimensionPixelSize(top)
        layoutParams.bottomMargin = context.resources.getDimensionPixelSize(bottom)
        layoutParams.marginEnd = context.resources.getDimensionPixelSize(right)
        layoutParams.marginStart = context.resources.getDimensionPixelSize(left)
        tab.layoutParams = layoutParams
        requestLayout()
    }
}