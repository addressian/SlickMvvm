package com.addressian.slickmvvm.bean

import androidx.lifecycle.MutableLiveData
import com.addressian.slickmvvm.base.BaseActionEvent
import kotlinx.coroutines.CoroutineScope

interface IViewModelAction {
    val lifecycleSupportedScope: CoroutineScope
    fun showLoadingDialog()
    fun updateLoadingDialogMessage(message: String)
    fun showLoadingDialog(message: String? = null)
    fun dismissLoadingDialog()
    fun showToast(message: String)
    fun finish()
    fun finishWithResultOk()
    fun getActionLiveData(): MutableLiveData<BaseActionEvent?>
    fun setActionLiveData(baseActionEvent: BaseActionEvent?)
}