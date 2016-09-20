package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal

abstract class EntityImpl : Entity {
    override fun dispose() {
        onDispose()
        disposed._emit(this)
    }

    protected open fun onDispose() = Unit

    override val disposed = Signal<Entity>()

    override val bounds: Rectangle2Di
        get() = Rectangle2Di(position.x, position.y, 128, 128)
}
