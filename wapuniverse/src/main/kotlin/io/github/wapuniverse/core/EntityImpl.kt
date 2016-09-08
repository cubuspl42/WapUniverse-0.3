package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Signal

abstract class EntityImpl : Entity {
    override fun dispose() {
        onDispose()
        disposed._emit(this)
    }

    protected open fun onDispose() = Unit

    override val disposed = Signal<Entity>()
}
