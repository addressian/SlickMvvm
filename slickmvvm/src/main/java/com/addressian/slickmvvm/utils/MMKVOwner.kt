package com.addressian.slickmvvm.utils

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A class that has a MMKV instance. If you want to customize the MMKV, you can override
 * the kv property. For example:
 *
 * ```kotlin
 * object DataRepository : MMKVOwner {
 *   override val kv = MMKV.mmkvWithID("MyID")
 * }
 * ```
 *
 * @author Dylan Cai
 */
interface MMKVOwner {
    val kv: MMKV
        get() = default
            ?: throw IllegalStateException("If you use MMKV in Application, you should set MMKVOwner.default first.")

    companion object {
        @JvmStatic
        var default: MMKV? = null

        fun initialize(context: Context) {
            if (default == null) {
                MMKV.initialize(context)
                default = MMKV.defaultMMKV()
            }
        }
    }
}

fun MMKVOwner.mmkvInt(default: Int = 0) =
    MMKVProperty({ kv.decodeInt(it, default) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvLong(default: Long = 0L) =
    MMKVProperty({ kv.decodeLong(it, default) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvBool(default: Boolean = false) =
    MMKVProperty({ kv.decodeBool(it, default) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvFloat(default: Float = 0f) =
    MMKVProperty({ kv.decodeFloat(it, default) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvDouble(default: Double = 0.0) =
    MMKVProperty({ kv.decodeDouble(it, default) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvString() =
    MMKVProperty({ kv.decodeString(it) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvString(default: String) =
    MMKVProperty({ kv.decodeString(it) ?: default }, { kv.encode(first, second) })

fun MMKVOwner.mmkvStringSet() =
    MMKVProperty({ kv.decodeStringSet(it) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvStringSet(default: Set<String>) =
    MMKVProperty({ kv.decodeStringSet(it) ?: default }, { kv.encode(first, second) })

fun MMKVOwner.mmkvBytes() =
    MMKVProperty({ kv.decodeBytes(it) }, { kv.encode(first, second) })

fun MMKVOwner.mmkvBytes(default: ByteArray) =
    MMKVProperty({ kv.decodeBytes(it) ?: default }, { kv.encode(first, second) })

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable() =
    MMKVProperty({ kv.decodeParcelable(it, T::class.java) }, { kv.encode(first, second) })

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable(default: T) =
    MMKVProperty(
        { kv.decodeParcelable(it, T::class.java) ?: default },
        { kv.encode(first, second) })

class MMKVProperty<V>(
    private val decode: (String) -> V,
    private val encode: Pair<String, V>.() -> Boolean
) : ReadWriteProperty<MMKVOwner, V> {

    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        decode(property.name)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        encode(property.name to value)
    }
}