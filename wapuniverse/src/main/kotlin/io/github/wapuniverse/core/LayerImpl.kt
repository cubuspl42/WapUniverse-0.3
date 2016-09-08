package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Signal

class LayerImpl : MutableLayer {
    private val _entities = hashSetOf<Entity>()

    override val entities = _entities

    override val onEntityAdded = Signal<Entity>()

    override fun addEntity(entity: Entity) {
        _entities.add(entity)
        onEntityAdded._emit(entity)
    }

    override val onEntityRemoved = Signal<Entity>()

    override fun removeEntity(entity: Entity) {
        entity.dispose()
        _entities.remove(entity)
        onEntityRemoved._emit(entity)
    }
}
