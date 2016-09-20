package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.Entity
import io.github.wapuniverse.core.EntitySelection
import io.github.wapuniverse.core.WorldEditor
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.view.EventHandlingStatus
import io.github.wapuniverse.view.SceneItem
import io.github.wapuniverse.view.SceneView
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class SelectionInputHandler(
        private val sceneView: SceneView,
        private val worldEditor: WorldEditor,
        wv: Vec2i) {

    val selection = worldEditor.startSelection(wv)

    private fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    val onDispose = Signal<SelectionInputHandler>()

    private fun dispose() {
        onDispose._emit(this)
    }

    fun onMouseReleased(ev: MouseEvent) {
        selection.commit()
        dispose()
    }

    fun onMouseDragged(ev: MouseEvent) {
        val wv = invTr(ev.x, ev.y)
        val minX = selection.area.minX
        val minY = selection.area.minY
        val w = Math.max(wv.x - minX, 0.0)
        val h = Math.max(wv.y - minY, 0.0)
        selection.updateArea(Rectangle2Di(minX, minY, w.toInt(), h.toInt()))
    }
}
