package io.github.wapuniverse.common

object XCollections {
    fun <E> xMutableSet(): XMutableSet<E> = XMutableSetImpl<E>(hashSetOf())
}
