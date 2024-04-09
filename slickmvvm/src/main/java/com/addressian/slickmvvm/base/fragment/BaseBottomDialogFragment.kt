package com.addressian.slickmvvm.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomDialogFragment : BaseDialogFragment() {

    open fun isFullHeight(): Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            setupFullWidth(sheet)
        }

        hideOnBackPressCall(dialog)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        initBehavior()
    }

    open fun initBehavior() {
        behavior = BottomSheetBehavior.from(requireView().parent as View).apply {
            isHideable = true
            isDraggable = true
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismissAllowingStateLoss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    private fun setupFullWidth(bottomSheet: View?) {
        val layoutParams = bottomSheet?.layoutParams
        if (layoutParams != null) {
            // Setting height to match_parent helps your dialog to be drawn above the inset of Navigation bar
            layoutParams.height = if (isFullHeight()) {
                WindowManager.LayoutParams.MATCH_PARENT
            } else {
                WindowManager.LayoutParams.WRAP_CONTENT
            }
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        bottomSheet?.layoutParams = layoutParams
    }

    private fun hideOnBackPressCall(dialog: Dialog) {
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (behavior.isHideable) {
                    hide()
                } else {
                    dismiss()
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }
}