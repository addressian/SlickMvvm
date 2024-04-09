package com.addressian.slickmvvm.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.full.primaryConstructor

class BaseViewModelFactory(
    private vararg val args: Any?
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return modelClass.kotlin.primaryConstructor?.call(*args)
            ?: throw IllegalArgumentException("$modelClass primaryConstructor is null")
    }

}