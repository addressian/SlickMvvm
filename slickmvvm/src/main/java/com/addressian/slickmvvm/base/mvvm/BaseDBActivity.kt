package com.addressian.slickmvvm.base.mvvm

import androidx.databinding.ViewDataBinding

abstract class BaseDBActivity<VB : ViewDataBinding> : BaseViewActivity() {

    private lateinit var _binding: VB

    override fun bindView() {
        _binding = getDataBinding()
        _binding.lifecycleOwner = this
        setContentView(_binding.root)
    }

    protected abstract fun getDataBinding(): VB
}