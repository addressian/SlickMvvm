package com.addressian.slickmvvm.base

import android.app.Application
import com.addressian.slickmvvm.utils.AppearanceUtils
import com.addressian.slickmvvm.utils.MMKVOwner

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKVOwner.initialize(this)
        AppearanceUtils.initNightMode()
    }

    companion object {
        private lateinit var instance: BaseApplication
        fun getInstance() = instance
    }
}
