package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.SmartObjectComponent
import io.github.wapuniverse.view.ResizeableVo
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import javafx.scene.Node


class SmartObjectPresenter(
        smartObjectComponent: SmartObjectComponent,
        sceneView: SceneView,
        sceneInputController: SceneInputController,
        sBoxComponent: SBoxComponent,
        root: Node) {

    init {
        smartObjectComponent.objectAdded.on { obj ->
            ResizeableVo(sceneView, sceneInputController, obj, sBoxComponent, root)
        }
    }
}
