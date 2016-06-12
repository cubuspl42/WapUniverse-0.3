package io.github.wapuniverse.editor


class EntityService(private val entityComponent: EntityComponent) {
    fun destroyEntities(entities: List<Entity>) {
        entities.forEach { entityComponent.removeEntity(it) }
    }
}
