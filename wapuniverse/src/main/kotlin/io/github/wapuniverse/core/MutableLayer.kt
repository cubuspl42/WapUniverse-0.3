package io.github.wapuniverse.core

interface MutableLayer : Layer {
    fun addEntity(repr: AdaptiveEntityRepr): AdaptiveEntity
    fun addEntity(repr: WapObjectRepr): WapObject
    fun addEntity(repr: EntityRepr): Entity
    fun removeEntity(entity: Entity)
}
