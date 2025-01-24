package com.sinasamaki.chromadecks.extensions

fun <T> MutableList<T>.moveItem(fromIndex: Int, toIndex: Int) {
    if (fromIndex !in indices || toIndex !in indices) {
        throw IndexOutOfBoundsException("Invalid indices: fromIndex=$fromIndex, toIndex=$toIndex")
    }
    val item = this.removeAt(fromIndex)
    this.add(toIndex, item)
}