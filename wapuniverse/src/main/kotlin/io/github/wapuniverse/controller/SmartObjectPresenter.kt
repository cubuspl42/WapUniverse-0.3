package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.Entity
import io.github.wapuniverse.editor.EntityComponent
import io.github.wapuniverse.editor.SmartObject
import io.github.wapuniverse.view.ResizeableVo
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import javafx.scene.Node


class SmartObjectPresenter(
        private val entityComponent: EntityComponent,
        private val sceneView: SceneView,
        private val sceneInputController: SceneInputController,
        private val sBoxComponent: SBoxComponent,
        private val root: Node) {

    init {
        entityComponent.entities.forEach { ent ->
            presentEntity(ent)
        }

        entityComponent.entityAdded.on { ent ->
            presentEntity(ent)
        }
    }

    private fun presentEntity(ent: Entity) {
        if (ent is SmartObject) {
            ResizeableVo(sceneView, sceneInputController, ent, sBoxComponent, root)
        }
    }
}
