package com.addressian.slickmvvm.extension

fun <T> Iterable<T>.pairIndexed(): Map<T, Int> {
    return this.withIndex().associate { it.value to it.index }
}