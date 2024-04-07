package com.addressian.slickmvvm.data.remove

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * 网络请求构建器接口
 */
interface INetworkApi {
    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(setHttpClientBuilder())
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    /**
     * 子类通过该方法配置 Okhttp
     */
    fun setHttpClientBuilder(): OkHttpClient

    /**
     * 子类通过该方法配置 Retrofit
     */
    fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder
}