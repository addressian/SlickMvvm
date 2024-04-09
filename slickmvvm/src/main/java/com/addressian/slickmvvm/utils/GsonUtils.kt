package com.addressian.slickmvvm.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object GsonUtils {
    val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setLenient()
        .create()

    @Throws(JsonSyntaxException::class)
    inline fun <reified T> fromJson(json: String?): T? {
        return try {
            gson.fromJson(json, object : TypeToken<T?>() {}.type)
        } catch (t: Throwable) {
            null
        }
    }

    @Throws(JsonSyntaxException::class)
    inline fun <reified T> fromJson(jsonElement: JsonElement?): T? {
        return try {
            gson.fromJson(jsonElement, object : TypeToken<T?>() {}.type)
        } catch (t: Throwable) {
            null
        }
    }

    fun toJson(src: Any?): String {
        return gson.toJson(src)
    }
}

fun Any.toJson() = GsonUtils.toJson(this)

inline fun <reified T : Any> String.fromJson(): T? = GsonUtils.fromJson(this)