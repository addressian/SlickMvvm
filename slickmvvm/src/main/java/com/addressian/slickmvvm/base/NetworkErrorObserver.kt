package com.addressian.slickmvvm.base

import com.addressian.slickmvvm.bean.NetworkError
import com.addressian.slickmvvm.bean.NetworkException

interface NetworkErrorObserver {
    fun handleApiError(netWorkException: NetworkException.ApiError<NetworkError>): Boolean {
        return false
    }

    fun handleUnKnownHostError(netWorkException: NetworkException.UnknownHostError): Boolean {
        return false
    }

    fun handleConnectError(netWorkException: NetworkException.ConnectError): Boolean {
        return false
    }
}
