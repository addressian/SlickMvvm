package com.addressian.slickmvvm.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.addressian.slickmvvm.data.local.themeConfig

object AppearanceUtils {

    fun initNightMode() {
        val nightMode = when {
            isFollowSystem() -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            else -> when (getAppearanceType()) {
                is AppearanceType.Dark -> {
                    AppCompatDelegate.MODE_NIGHT_YES
                }

                is AppearanceType.Light -> {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    fun getNightMode(): NightMode {
        return when {
            isFollowSystem() -> {
                NightMode.FollowSystem
            }

            else -> when (getAppearanceType()) {
                is AppearanceType.Dark -> {
                    NightMode.Night
                }

                is AppearanceType.Light -> {
                    NightMode.Day
                }
            }
        }
    }

    fun setNightMode(nightMode: NightMode) {
        fun setFollowSystem(b: Boolean) {
            themeConfig.IS_FOLLOW_SYSTEM = b
        }

        fun setAppearanceType(appearanceType: AppearanceType) {
            themeConfig.THEME_TYPE = appearanceType.value
        }
        when (nightMode) {
            is NightMode.FollowSystem -> {
                setFollowSystem(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            is NightMode.Day -> {
                setFollowSystem(false)
                setAppearanceType(AppearanceType.Light)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            is NightMode.Night -> {
                setFollowSystem(false)
                setAppearanceType(AppearanceType.Dark)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    fun isNight(context: Context) =
        when {
            isFollowSystem() -> {
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> true
                    Configuration.UI_MODE_NIGHT_NO -> false
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                    else -> false
                }
            }

            else -> when (getAppearanceType()) {
                is AppearanceType.Dark -> true
                is AppearanceType.Light -> false
            }
        }

    private fun isFollowSystem() = themeConfig.IS_FOLLOW_SYSTEM

    private fun getAppearanceType() = when (themeConfig.THEME_TYPE) {
        AppearanceType.Light.value -> {
            AppearanceType.Light
        }

        AppearanceType.Dark.value -> {
            AppearanceType.Dark
        }

        else -> {
            throw IllegalArgumentException("appearance that do not exit")
        }
    }

    sealed class NightMode {
        object FollowSystem : NightMode()
        object Day : NightMode()
        object Night : NightMode()
    }

    sealed class AppearanceType {
        open val value: String = ""

        object Light : AppearanceType() {
            override val value: String = "light"
        }

        object Dark : AppearanceType() {
            override val value: String = "dark"
        }
    }
}