package io.github.wapuniverse.presenter

import io.github.wapuniverse.core.AdaptiveEntity
import io.github.wapuniverse.core.Entity
import io.github.wapuniverse.core.Layer
import io.github.wapuniverse.core.WapObject
import io.github.wapuniverse.view.LayerNode

class LayerPresenter(
        private val layer: Layer,
        private val layerNode: LayerNode,
        private val worldPresenter: WorldPresenter
) {
    val entityPresenters = hashSetOf<EntityPresenter>()

    private fun makeEntityPresenter(entity: Entity) = when (entity) {
        is AdaptiveEntity -> AdaptiveEntityPresenter(entity, layerNode)
        is WapObject -> WapObjectPresenter(entity, layerNode, worldPresenter)
        else -> throw IllegalArgumentException()
    }

    private fun addEntityPresenter(entity: Entity) = entityPresenters.add(makeEntityPresenter(entity))

    init {
        layer.entities.forEach { addEntityPresenter(it) }
        layer.onEntityAdded.on { addEntityPresenter(it) }
    }
}
