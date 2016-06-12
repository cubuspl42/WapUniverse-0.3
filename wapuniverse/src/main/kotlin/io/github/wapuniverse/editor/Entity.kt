package io.github.wapuniverse.editor

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.*
import java.util.*


abstract class Entity {
    abstract var position: Vec2i

    fun dispose() {
        onDispose()
        disposed._emit(this)
    }

    protected open fun onDispose() = Unit

    val disposed = Signal<Entity>()
}

class EntityComponent {
    private val _entities = HashSet<Entity>()

    val entities: Set<Entity>
        get() = _entities

    fun addEntity(ent: Entity) {
        _entities.add(ent)
        entityAdded._emit(ent)
    }

    val entityAdded = Signal<Entity>()

    fun removeEntity(ent: Entity) {
        ent.dispose()
        _entities.remove(ent)
        entityRemoved._emit(ent)
    }

    val entityRemoved = Signal<Entity>()
}
