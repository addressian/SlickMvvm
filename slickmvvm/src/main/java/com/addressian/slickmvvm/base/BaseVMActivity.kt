package com.addressian.slickmvvm.base

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.addressian.slickmvvm.R
import com.addressian.slickmvvm.bean.IViewModelAction
import com.addressian.slickmvvm.bean.NetworkError
import com.addressian.slickmvvm.bean.NetworkException
import com.addressian.slickmvvm.extension.showToast

abstract class BaseVMActivity : AppCompatActivity(), NetworkErrorDispatcher {

    suspend fun <T> load(execute: suspend () -> T): T {
        this.showLoadingDialog()
        return execute().also {
            this.dismissLoadingDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelEvent()
    }

    private fun initViewModelEvent() {
        val viewModelList = initViewModelList()
        if (!viewModelList.isNullOrEmpty()) {
            observeEvent(viewModelList)
        }
    }

    protected open fun initViewModel(): ViewModel? = null

    protected open fun initViewModelList(): List<ViewModel>? =
        initViewModel()?.let { listOf(it) }

    private val networkErrorObservers = mutableListOf<NetworkErrorObserver>()
    override fun addNetworkErrorObserver(observer: NetworkErrorObserver) {
        networkErrorObservers.add(observer)
    }

    override fun removeNetworkErrorObserver(observer: NetworkErrorObserver) {
        networkErrorObservers.remove(observer)
    }

    private fun observeEvent(viewModelList: List<ViewModel>) {
        for (viewModel in viewModelList) {
            if (viewModel is IViewModelAction) {
                viewModel
                    .getActionLiveData()
                    .observe(
                        this
                    ) {
                        if (it == null) return@observe
                        when (it.action) {
                            BaseActionEvent.SHOW_LOADING_DIALOG -> {
                                showLoadingDialog(it.message)
                            }

                            BaseActionEvent.UPDATE_LOADING_DIALOG_MSG -> {
                                updateLoadingDialogMessage(it.message)
                            }

                            BaseActionEvent.DISMISS_LOADING_DIALOG -> {
                                dismissLoadingDialog()
                            }

                            BaseActionEvent.SHOW_TOAST -> {
                                it.message?.let { strId ->
                                    showToast(strId)
                                }
                            }

                            BaseActionEvent.FINISH -> {
                                finish()
                            }

                            BaseActionEvent.FINISH_WITH_RESULT_OK -> {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }

                            BaseActionEvent.Error_UnKnown -> {
                                dismissLoadingDialog()
                                showToast(getString(R.string.toast_the_client_is_abnormal_please_check_app_update))
                                it.reportException()
                            }

                            else -> {
                                dismissLoadingDialog()
                                it.reportException()
                                dispatchNetworkError(it)
                            }
                        }
                        viewModel.setActionLiveData(null)
                    }
            }
        }
    }

    private fun handleNetworkError(it: BaseActionEvent) {
        when (it.action) {
            BaseActionEvent.Error_Net_Unknown_Host -> {
                handleUnKnownHostError(it.netWorkException as NetworkException.UnknownHostError)
            }

            BaseActionEvent.Error_Net_Connect -> {
                handleConnectError(it.netWorkException as NetworkException.ConnectError)
            }

            BaseActionEvent.Error_Net_Api -> {
                preHandleApiError(it.netWorkException as NetworkException.ApiError<NetworkError>)
            }
        }
        onHandleNetworkErrorCompletion()
    }

    private fun dispatchNetworkError(it: BaseActionEvent) {
        var isIntercept = false

        when (it.action) {
            BaseActionEvent.Error_Net_Unknown_Host -> {
                networkErrorObservers.onEach { observer ->
                    isIntercept =
                        observer.handleUnKnownHostError(it.netWorkException as NetworkException.UnknownHostError)
                }
            }

            BaseActionEvent.Error_Net_Connect -> {
                networkErrorObservers.onEach { observer ->
                    isIntercept =
                        observer.handleConnectError(it.netWorkException as NetworkException.ConnectError)
                }
            }

            BaseActionEvent.Error_Net_Api -> {
                networkErrorObservers.onEach { observer ->
                    isIntercept =
                        observer.handleApiError(it.netWorkException as NetworkException.ApiError<NetworkError>)
                }
            }
        }
        if (!isIntercept) {
            handleNetworkError(it)
        }
    }

    private fun preHandleApiError(netWorkException: NetworkException.ApiError<NetworkError>) {
        if (preHandlerApiError(netWorkException)) {
            return
        }
        handleApiError(netWorkException)
    }

    open fun preHandlerApiError(netWorkException: NetworkException.ApiError<NetworkError>): Boolean {
        return false
    }

    open fun onHandleNetworkErrorCompletion() = Unit

    open fun handleApiError(netWorkException: NetworkException.ApiError<NetworkError>) {
        showToast(netWorkException.body.message)
    }

    open fun handleUnKnownHostError(netWorkException: NetworkException.UnknownHostError) {
        showToast(R.string.toast_network_connection_failed)
    }

    open fun handleConnectError(netWorkException: NetworkException.ConnectError) {
        showToast(R.string.toast_network_connection_failed)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
    }

    @Suppress("DEPRECATION")
    private var progressDialog: ProgressDialog? = null

    @Suppress("DEPRECATION")
    open fun showLoadingDialog(message: String? = null) {
        (progressDialog ?: ProgressDialog(this, R.style.LoadingDialogStyle).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            progressDialog = this
        }).let { dialog ->
            dialog.setMessage(message ?: "")
            dialog.show()
        }
    }

    @Suppress("DEPRECATION")
    open fun updateLoadingDialogMessage(message: String? = null) {
        progressDialog?.let {
            if (it.isShowing) {
                it.setMessage(message ?: "")
            }
        }
    }

    open fun dismissLoadingDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
                progressDialog = null
            }
        }
    }

    protected val context
        get() = this@BaseVMActivity

    protected val isFinishingOrDestroyed: Boolean
        get() = isFinishing || isDestroyed
}