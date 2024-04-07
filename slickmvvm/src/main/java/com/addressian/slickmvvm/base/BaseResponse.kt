package com.addressian.slickmvvm.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T>(
    @SerializedName("data")
    val data: T,
    @SerializedName("errors")
    val errs: List<Error>,
    @SerializedName("success")
    val success: Boolean
)
