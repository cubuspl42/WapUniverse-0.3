package io.github.wapuniverse.editor

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.utils.*
import java.util.*


abstract class Entity {
    abstract var position: Vec2i
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
}
