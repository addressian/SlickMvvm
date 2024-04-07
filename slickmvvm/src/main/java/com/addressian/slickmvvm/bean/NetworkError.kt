package com.addressian.slickmvvm.bean

import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("info")
    val info: String?,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: String
)