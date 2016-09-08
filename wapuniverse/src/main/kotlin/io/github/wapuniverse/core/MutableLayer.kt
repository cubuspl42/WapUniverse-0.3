package io.github.wapuniverse.core

interface MutableLayer : Layer {
    fun addEntity(entity: Entity)
    fun removeEntity(entity: Entity)
}
