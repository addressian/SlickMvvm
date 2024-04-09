package com.addressian.slickmvvm.base

import android.app.Application
import com.addressian.slickmvvm.utils.AppearanceUtils
import com.addressian.slickmvvm.utils.MMKVOwner

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKVOwner.initialize(this)
        AppearanceUtils.initNightMode()
    }

}
