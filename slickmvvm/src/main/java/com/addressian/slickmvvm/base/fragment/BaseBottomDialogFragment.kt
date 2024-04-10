package com.addressian.slickmvvm.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomDialogFragment : BaseDialogFragment() {

    open fun isFullHeight(): Boolean = false
    open fun getCancelable() = false
    open fun getCanceledOnTouchOutside() = false
    open fun isHideAble() = true
    open fun getDraggable() = true
    open fun getState() = BottomSheetBehavior.STATE_EXPANDED
    open fun isSkipCollapsed() = true
    open fun canGoBack() = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (!canGoBack()) {
            disableGoBack(dialog)
        }
        dialog.setCancelable(getCancelable())
        dialog.setCanceledOnTouchOutside(getCanceledOnTouchOutside())
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            setupFullWidth(sheet)
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        configBottomSheetBehavior()
    }

    open fun configBottomSheetBehavior() {
        BottomSheetBehavior.from(requireView().parent as View).let {
            it.isHideable = isHideAble()
            it.isDraggable = getDraggable()
            it.state = getState()
            it.skipCollapsed = isSkipCollapsed()
            it.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismissAllowingStateLoss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            })
            behavior = it
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

    private fun disableGoBack(dialog: Dialog) {
        dialog.setOnKeyListener { _, _, _ ->
            return@setOnKeyListener true
        }
    }
}