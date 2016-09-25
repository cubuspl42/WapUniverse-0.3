package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.Layer
import io.github.wapuniverse.core.WorldEditor
import io.github.wapuniverse.utils.toVec2i

class SelectionController(
        worldController: WorldController,
        worldEditor: WorldEditor,
        ev: Vec2d
) {
    val entitySelection = worldEditor.startSelection(worldController.invTr(ev.x, ev.y).toVec2i())


}