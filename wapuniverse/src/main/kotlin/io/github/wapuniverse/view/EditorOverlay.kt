package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.Transform
import io.github.wapuniverse.common.util.Vec2d
import io.github.wapuniverse.common.util.position
import javafx.scene.layout.Pane

class EditorOverlay(
        private val worldScene: DScene
) : Pane() {
    var worldConstraint: Vec2d? = null

    var scale = 1.0

    init {
        setOnMousePressed { ev ->
            worldConstraint = mapViewConstraint(ev.position)
        }

        setOnMouseDragged { ev ->
            updateTransform(ev.position, worldConstraint!!, scale)
        }

        setOnScroll { ev ->
            val scaleMultiplier = when {
                ev.deltaY > 0 -> 2.0
                else -> 0.5
            }
            scale *= scaleMultiplier

            updateTransform(ev.position, mapViewConstraint(ev.position), scale)
        }
    }

    private fun mapViewConstraint(viewConstraint: Vec2d): Vec2d {
        return worldScene.invertedTransform.map(viewConstraint)
    }

    private fun updateTransform(viewConstraint: Vec2d, worldConstraint: Vec2d, scale: Double) {
        val translate = -(worldConstraint - (viewConstraint / scale))
        worldScene.transform = Transform(translate, scale)
    }
}
