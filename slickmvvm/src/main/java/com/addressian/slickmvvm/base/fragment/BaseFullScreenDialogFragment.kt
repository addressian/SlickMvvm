package com.addressian.slickmvvm.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.addressian.slickmvvm.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseFullScreenDialogFragment : BaseDialogFragment() {
    override fun getTheme() = R.style.FullDialogBackgroundDialogStyle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            setupFullScreen(sheet)

            behavior = BottomSheetBehavior.from(sheet!!).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                isHideable = true
                isDraggable = false
                skipCollapsed = true
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
        }

        hideOnBackPressCall(dialog)

        return dialog
    }

    private fun setupFullScreen(bottomSheet: View?) {
        val layoutParams = bottomSheet?.layoutParams
        if (layoutParams != null) {
            // Setting height to match_parent helps your dialog to be drawn above the inset of Navigation bar
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        bottomSheet?.layoutParams = layoutParams
    }

    private fun hideOnBackPressCall(dialog: Dialog) {
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                hide()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }
}