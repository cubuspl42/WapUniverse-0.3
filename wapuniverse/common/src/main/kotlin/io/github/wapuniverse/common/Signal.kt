package io.github.wapuniverse.common

interface Signal<out T> {
    fun connect(f: (T) -> Unit)
    fun disconnect(f: (T) -> Unit)
}
