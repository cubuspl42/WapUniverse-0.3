package io.github.wapuniverse.core


class EntityService(private val entityComponent: EntityComponent) {
    fun destroyEntities(entities: List<Entity>) {
        entities.forEach { entityComponent.removeEntity(it) }
    }
}
