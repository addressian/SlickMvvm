package com.addressian.slickmvvm.base.fragment

import android.content.DialogInterface
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialogFragment : BottomSheetDialogFragment(){
    lateinit var behavior: BottomSheetBehavior<View>

    fun interface OnDismissListener{
        fun onDismiss()
    }
    private var onDismissListener: OnDismissListener?=null
    fun setOnDismissListener(onDismissListener: OnDismissListener){
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss()
    }

    fun hide(){
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
}