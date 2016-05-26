package io.github.wapuniverse.utils


class Signal<T> {
    val listeners: MutableList<(T) -> Unit> = mutableListOf()

    fun connect(f: (T) -> Unit) {
        listeners.add(f)
    }

    fun disconnect(f: (T) -> Unit) {
        listeners.remove(f)
    }

    fun _emit(t: T) {
        listeners.forEach { f-> f(t) }
    }
}
