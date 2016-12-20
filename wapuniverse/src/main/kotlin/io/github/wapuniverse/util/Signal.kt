package io.github.wapuniverse.util


class Signal<T> {
    val listeners: MutableList<(T) -> Unit> = mutableListOf()

    fun on(f: (T) -> Unit): (T) -> Unit {
        listeners.add(f)
        return f
    }

    fun off(f: (T) -> Unit) {
        listeners.remove(f)
    }

    fun _emit(t: T) {
        listeners.forEach { f-> f(t) }
    }
}
