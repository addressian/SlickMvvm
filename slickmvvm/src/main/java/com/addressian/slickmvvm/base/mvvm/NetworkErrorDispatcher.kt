package com.addressian.slickmvvm.base.mvvm

interface NetworkErrorDispatcher {
    fun addNetworkErrorObserver(observer: NetworkErrorObserver)
    fun removeNetworkErrorObserver(observer: NetworkErrorObserver)
}