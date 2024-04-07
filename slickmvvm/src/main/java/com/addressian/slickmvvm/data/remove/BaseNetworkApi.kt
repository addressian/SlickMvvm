package com.addressian.slickmvvm.data.remove

import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络请求基础构建器
 * 继承此类，编写创建请求头拦截器逻辑，其他方法按需 override
 */
abstract class BaseNetworkApi : INetworkApi {

    companion object {
        const val TAG_RETROFIT = "Retrofit"
        const val TAG_CURL = "curl"

        const val CONNECT_TIME_OUT = 15L
        const val READ_TIME_OUT = 120L
        const val WRITE_TIME_OUT = 60L
    }

    open fun getConnectTimeOut() = CONNECT_TIME_OUT
    open fun getReadTimeOut() = READ_TIME_OUT
    open fun getWriteTimeOut() = WRITE_TIME_OUT
    open fun getTimeUnit() = TimeUnit.SECONDS

    override fun setHttpClientBuilder() = OkHttpClient.Builder().config().cache(null).build()

    override fun setRetrofitBuilder(builder: Retrofit.Builder) = builder.apply {
        val gsonBuilder = Gson().newBuilder().setLenient().registerTypeAdapters().create()
        addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    open fun GsonBuilder.registerTypeAdapters(
    ): GsonBuilder = this

    open fun OkHttpClient.Builder.config(): OkHttpClient.Builder {
        retryOnConnectionFailure(true)
        connectTimeout(getConnectTimeOut(), getTimeUnit())
        readTimeout(getReadTimeOut(), getTimeUnit())
        writeTimeout(getWriteTimeOut(), getTimeUnit())
        addInterceptor(createHeaderInterceptor()) // 在日志拦截器之前添加 token，让日志显示 token
            .apply {
                if (AppUtils.isAppDebug()) {
                    addNetworkInterceptor(createLoggingInterceptor())
                    addInterceptor(createCurlInterceptor())
                }
            }
        return this
    }

    abstract fun createHeaderInterceptor(): Interceptor

    open fun createLoggingInterceptor() =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.i(
                    TAG_RETROFIT, message
                )
            }
        }).apply {
            level = getHttpLoggingInterceptorLever()
        }

    open fun getHttpLoggingInterceptorLever() = HttpLoggingInterceptor.Level.BODY

    open fun createCurlInterceptor() =
        CurlInterceptor { message ->
            LogUtils.dTag(TAG_CURL, message)
        }
}