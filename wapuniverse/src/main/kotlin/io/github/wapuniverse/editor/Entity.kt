package io.github.wapuniverse.editor

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.utils.*
import java.util.*


abstract class Entity {
    abstract var position: Vec2i

    abstract val boundingBox: Rectangle2Di

    val isSelected: Boolean
        get() = _isSelected

    val isHovered: Boolean
        get() = _isHovered

    open fun onSelected() = Unit

    open fun onUnselected() = Unit

    open fun onHover() = Unit

    open fun onUnhover() = Unit

    internal var _isSelected = false

    internal var _isHovered = false
}

class EntityComponent {
    private val _entities = HashSet<Entity>()

    val entities: Set<Entity>
        get() = _entities

    fun addEntity(ent: Entity) {
        _entities.add(ent)
        entityAdded._emit(ent)
    }

    fun selectableEntitiesAt(x: Int, y: Int): List<Entity> {
        return _entities.filter { it.boundingBox.contains(x, y) }
    }


    val entityAdded = Signal<Entity>()
}
