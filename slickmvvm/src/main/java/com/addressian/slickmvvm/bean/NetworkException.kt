package com.addressian.slickmvvm.bean

import java.io.IOException

sealed class NetworkException<out T : Any> {
    /**
     * Failure response with body
     */
    data class ApiError<T : Any>(val body: T, val code: Int) : NetworkException<T>() {
        companion object {
            const val UNAUTHORIZED_CODE = 401
        }

        fun isUnAuthorizedCode(): Boolean {
            return code == UNAUTHORIZED_CODE
        }

        fun isServerError(): Boolean {
            return code in 500..599
        }

        fun isClientError(): Boolean {
            return code in 400..499
        }
    }

    data class ConnectError(val error: IOException) : NetworkException<Nothing>()

    data class UnknownHostError(val error: Throwable?) : NetworkException<Nothing>()
}