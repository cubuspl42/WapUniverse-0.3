package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.EntityComponent
import io.github.wapuniverse.editor.SmartObject
import io.github.wapuniverse.view.ResizeableVo
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import javafx.scene.Node


class SmartObjectPresenter(
        entityComponent: EntityComponent,
        sceneView: SceneView,
        sceneInputController: SceneInputController,
        sBoxComponent: SBoxComponent,
        root: Node) {

    init {
        entityComponent.entityAdded.on { ent ->
            if (ent is SmartObject) {
                ResizeableVo(sceneView, sceneInputController, ent, sBoxComponent, root)
            }
        }
    }
}
