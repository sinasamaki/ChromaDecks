package com.sinasamaki.chromadecks.extensions

fun <T> MutableList<T>.moveItem(fromIndex: Int, toIndex: Int) {
    if (isEmpty()) return
    val safeFrom = fromIndex.coerceIn(indices)
    val item = this.removeAt(safeFrom)
    this.add(toIndex.coerceIn(0, size), item)
}