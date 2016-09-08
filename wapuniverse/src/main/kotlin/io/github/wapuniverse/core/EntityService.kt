package io.github.wapuniverse.core


class EntityService(private val layer: MutableLayer) {
    fun destroyEntities(entities: List<Entity>) {
        entities.forEach { layer.removeEntity(it) }
    }
}
