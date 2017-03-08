package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.Transform
import io.github.wapuniverse.common.util.Vec2d
import io.github.wapuniverse.common.util.position
import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane

class EditorOverlay(
        private val worldScene: DScene
) : Pane() {
    var worldConstraint: Vec2d? = null

    var scale = 1.0

    init {
        addEventFilter(MouseEvent.MOUSE_PRESSED, { ev ->
            if (ev.button == MouseButton.SECONDARY) {
                worldConstraint = mapViewConstraint(ev.position)
                ev.consume()
            }
        })

        addEventFilter(MouseEvent.MOUSE_DRAGGED, { ev ->
            if (ev.button == MouseButton.SECONDARY) {
                updateTransform(ev.position, worldConstraint!!, scale)
                ev.consume()
            }
        })

        addEventFilter(MouseEvent.MOUSE_RELEASED, { ev ->
            if (ev.button == MouseButton.SECONDARY) {
                ev.consume()
            }
        })

        addEventFilter(ScrollEvent.SCROLL, { ev ->
            val scaleMultiplier = when {
                ev.deltaY > 0 -> 2.0
                else -> 0.5
            }
            scale *= scaleMultiplier

            updateTransform(ev.position, mapViewConstraint(ev.position), scale)
            ev.consume()
        })
    }

    private fun mapViewConstraint(viewConstraint: Vec2d): Vec2d {
        return worldScene.invertedTransform.map(viewConstraint)
    }

    private fun updateTransform(viewConstraint: Vec2d, worldConstraint: Vec2d, scale: Double) {
        val translate = -(worldConstraint - (viewConstraint / scale))
        worldScene.transform = Transform(translate, scale)
    }
}
