package com.addressian.slickmvvm.base

import com.addressian.slickmvvm.bean.NetworkError
import com.addressian.slickmvvm.bean.NetworkException
import com.addressian.slickmvvm.utils.ReportUtils

class BaseActionEvent(var action: Int) {
    var message: String? = null
    var exception: Throwable? = null
    var netWorkException: NetworkException<NetworkError>? = null

    fun reportException() {
        exception?.let {
            ReportUtils.reportException(it)
        }
    }

    companion object {
        const val SHOW_LOADING_DIALOG = 1
        const val UPDATE_LOADING_DIALOG_MSG = 2
        const val DISMISS_LOADING_DIALOG = 3
        const val SHOW_TOAST = 4
        const val SHOW_TOAST_LONG = 5
        const val FINISH = 6

        const val FINISH_WITH_RESULT_OK = 7

        // 网络错误
        const val Error_Net_Connect = 8
        const val Error_Net_Unknown_Host = 9
        const val Error_Net_Api = 10

        // 未知错误
        const val Error_UnKnown = 11
    }
}