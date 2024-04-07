package com.addressian.slickmvvm.base

interface NetworkErrorDispatcher {
    fun addNetworkErrorObserver(observer: NetworkErrorObserver)
    fun removeNetworkErrorObserver(observer: NetworkErrorObserver)
}