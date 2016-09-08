package io.github.wapuniverse.core

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.wap32.WwdObject
import java.util.*


abstract class Entity {
    abstract var position: Vec2i

    fun dispose() {
        onDispose()
        disposed._emit(this)
    }

    protected open fun onDispose() = Unit

    val disposed = Signal<Entity>()

    abstract fun dump(): WwdObject
}
