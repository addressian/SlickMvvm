package com.addressian.slickmvvm.base.mvvm

import com.addressian.slickmvvm.bean.ResultWrapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.UnknownHostException

/**
 * 基础数据层
 */
open class BaseRepository {

    fun createFormDataParts(name: String, tempFile: File) =
        MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart(
                name,
                filename = tempFile.name,
                body = tempFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }.build().parts

    /**
     * 捕获网络异常信息
     */
    protected suspend fun <T> getResult(call: suspend () -> T): ResultWrapper<T> {
        try {
            return ResultWrapper.Success(call.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is UnknownHostException -> return ResultWrapper.NetworkError
                is IOException -> return ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorMsg = if (code == 400) {
                        try {
                            val errorBody = throwable.response()?.errorBody()!!.string()
                            val result = JSONObject(errorBody)
                            val errors = result.getJSONArray("errors")
                            JSONObject(errors[0].toString()).getString("message")
                        } catch (e: Exception) {
                            "parse error: ${e.message.toString()}"
                        }
                    } else {
                        "errorCode: $code"
                    }
                    return ResultWrapper.GenericError(code, errorMsg)
                }
                else -> {
                    return ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}