package com.addressian.slickmvvm.extension

import com.addressian.slickmvvm.bean.NetworkError
import org.json.JSONObject
import retrofit2.HttpException

fun HttpException.parseToErrorBody() = try {
    val jsonObject =
        JSONObject(
            response()
                ?.errorBody()!!
                .string()
        )
    val errorArr = jsonObject.getJSONArray("errors")
    val errorObject = JSONObject(
        errorArr
            .get(0)
            .toString()
    )
    val mInfo: String? = try {
        errorObject.getString("info")
    } catch (e: Throwable) {
        null
    }
    val mMessage: String = errorObject.getString("message")
    val mCode: String = errorObject.getString("code")
    NetworkError(mInfo, mMessage, mCode)
} catch (ex: Exception) {
    null
}