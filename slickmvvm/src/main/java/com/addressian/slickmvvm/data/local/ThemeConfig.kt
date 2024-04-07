package com.addressian.slickmvvm.data.local

import com.addressian.slickmvvm.utils.AppearanceUtils
import com.addressian.slickmvvm.utils.MMKVOwner
import com.addressian.slickmvvm.utils.mmkvBool
import com.addressian.slickmvvm.utils.mmkvString
import com.tencent.mmkv.MMKV

val themeConfig: ThemeConfig
    get() = ThemeConfig.getInstance()

class ThemeConfig private constructor() : MMKVOwner {

    override val kv: MMKV = MMKV.mmkvWithID(FILE_ID)

    var IS_FOLLOW_SYSTEM: Boolean by mmkvBool(true)

    var THEME_TYPE: String by mmkvString(AppearanceUtils.AppearanceType.Light.value)

    companion object {
        const val FILE_ID = "AppearanceConfig"
        private var instance: ThemeConfig? = null
        fun getInstance(): ThemeConfig {
            return instance ?: synchronized(this) {
                instance ?: ThemeConfig()
            }
        }

        fun destroy() {
            instance = null
        }
    }
}