package io.github.wapuniverse.common

class Emitter<T> : Signal<T> {
    private val slots = mutableSetOf<(T) -> Unit>()

    override fun connect(f: (T) -> Unit) {
        slots.add(f)
    }

    override fun disconnect(f: (T) -> Unit) {
        slots.remove(f)
    }

    operator fun invoke(value: T) {
        slots.forEach { it(value) }
    }
}
