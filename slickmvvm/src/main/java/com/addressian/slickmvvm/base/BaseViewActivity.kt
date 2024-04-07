package com.addressian.slickmvvm.base

import android.os.Bundle

abstract class BaseViewActivity : BaseVMActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beforeSetView()

        bindView()

        initToolbar()

        initCreate()

        registerEventBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterEventBus()
    }

    open fun beforeSetView() {}

    abstract fun bindView()

    /**
     * 初始化自定义 toolbar
     */
    protected open fun initToolbar() {}

    abstract fun initCreate()

    protected open fun registerEventBus() {}

    protected open fun unRegisterEventBus() {}

    fun backgroundAlpha(f: Float) {
        val lp = window.attributes
        lp.alpha = f
        window.attributes = lp
    }
}