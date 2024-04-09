package com.addressian.slickmvvm.base.mvvm

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.addressian.slickmvvm.base.BaseApplication
import com.addressian.slickmvvm.bean.IViewModelAction
import com.addressian.slickmvvm.bean.NetworkError
import com.addressian.slickmvvm.bean.NetworkException
import com.addressian.slickmvvm.extension.parseToErrorBody
import com.blankj.utilcode.util.StringUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application),
    IViewModelAction {

    override val lifecycleSupportedScope: CoroutineScope
        get() = viewModelScope

    private var actionLiveData: MutableLiveData<BaseActionEvent?> = MutableLiveData()
    override fun getActionLiveData(): MutableLiveData<BaseActionEvent?> {
        return actionLiveData
    }

    override fun setActionLiveData(baseActionEvent: BaseActionEvent?) {
        actionLiveData.value = baseActionEvent
    }

    /**
     * des 在函数执行期间显示加载框
     */
    fun <T> load(
        message: String? = null,
        execute: () -> T
    ): T {
        this.showLoadingDialog(message)
        return execute().also {
            this.dismissLoadingDialog()
        }
    }

    fun launch(execute: suspend () -> Unit) =
        viewModelScope.launch(exceptionHandler) {
            execute()
        }

    val exceptionHandler = CoroutineExceptionHandler { _, t ->
        actionLiveData.value = exceptionParser(t)
    }

    val ioExceptionHandler = CoroutineExceptionHandler { _, t ->
        actionLiveData.postValue(exceptionParser(t))
    }

    private fun exceptionParser(t: Throwable) =
        when (t) {
            is UnknownHostException -> {
                BaseActionEvent(BaseActionEvent.Error_Net_Unknown_Host)
                    .apply {
                        netWorkException = NetworkException.UnknownHostError(t)
                    }
            }

            is IOException -> {
                BaseActionEvent(BaseActionEvent.Error_Net_Connect)
                    .apply {
                        netWorkException = NetworkException.ConnectError(t)
                    }
            }

            is HttpException -> {
                val code = t.code()
                val error = t
                    .response()
                    ?.errorBody()
                val errorBody =
                    when {
                        error == null -> {
                            null
                        }

                        error.contentLength() == 0L -> {
                            null
                        }

                        else -> t.parseToErrorBody()
                    }

                BaseActionEvent(BaseActionEvent.Error_Net_Api)
                    .apply {
                        netWorkException = NetworkException.ApiError(
                            errorBody ?: NetworkError(null, "", code.toString()),
                            code
                        )
                    }
            }

            else -> {
                BaseActionEvent(BaseActionEvent.Error_UnKnown)
                    .apply {
                        this.exception = t
                    }
            }
        }

    override fun showLoadingDialog() {
        showLoadingDialog(null)
    }

    override fun showLoadingDialog(message: String?) {
        val baseActionEvent =
            BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG)
        baseActionEvent.message = message
        actionLiveData.value = baseActionEvent
    }

    override fun updateLoadingDialogMessage(message: String) {
        val baseActionEvent =
            BaseActionEvent(BaseActionEvent.UPDATE_LOADING_DIALOG_MSG)
        baseActionEvent.message = message
        actionLiveData.value = baseActionEvent
    }

    override fun dismissLoadingDialog() {
        actionLiveData.value =
            BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG)
    }

    override fun showToast(message: String) {
        val baseActionEvent =
            BaseActionEvent(BaseActionEvent.SHOW_TOAST)
        baseActionEvent.message = message
        actionLiveData.value = baseActionEvent
    }

    fun showToast(@StringRes resIdRes: Int, toastLength: Int = BaseActionEvent.SHOW_TOAST) {
        val baseActionEvent =
            BaseActionEvent(toastLength)
        baseActionEvent.message = StringUtils.getString(resIdRes)
        actionLiveData.value = baseActionEvent
    }

    override fun finish() {
        actionLiveData.value =
            BaseActionEvent(BaseActionEvent.FINISH)
    }

    override fun finishWithResultOk() {
        actionLiveData.value =
            BaseActionEvent(BaseActionEvent.FINISH_WITH_RESULT_OK)
    }
}