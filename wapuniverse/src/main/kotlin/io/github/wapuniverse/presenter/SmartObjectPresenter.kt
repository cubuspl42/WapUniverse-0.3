package io.github.wapuniverse.presenter

import io.github.wapuniverse.controller.SceneInputController
import io.github.wapuniverse.core.Entity
import io.github.wapuniverse.core.AdaptiveEntity
import io.github.wapuniverse.core.Layer
import io.github.wapuniverse.view.ResizeableVo
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import javafx.scene.Node


class SmartObjectPresenter(
        layer: Layer,
        private val sceneView: SceneView,
        private val sceneInputController: SceneInputController,
        private val sBoxComponent: SBoxComponent,
        private val node: Node) {

    init {
        layer.entities.forEach { ent ->
            presentEntity(ent)
        }

        layer.onEntityAdded.on { ent ->
            presentEntity(ent)
        }
    }

    private fun presentEntity(ent: Entity) {
        if (ent is AdaptiveEntity) {
            ResizeableVo(sceneView, sceneInputController, ent, sBoxComponent, node)
        }
    }
}
