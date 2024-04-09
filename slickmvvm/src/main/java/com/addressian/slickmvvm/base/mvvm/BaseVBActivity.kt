package com.addressian.slickmvvm.base.mvvm

import androidx.viewbinding.ViewBinding
import com.addressian.slickmvvm.extension.updateStatusBarColor

abstract class BaseVBActivity<VB : ViewBinding> : BaseViewActivity() {

    private lateinit var _binding: VB

    override fun bindView() {
        _binding = getDataBinding()
        setContentView(_binding.root)
        updateStatusBarColor(_binding.root)
    }

    protected abstract fun getDataBinding(): VB
}