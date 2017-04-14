package io.github.wapuniverse.view

import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.Signal
import io.github.wapuniverse.common.util.Transform
import io.github.wapuniverse.common.util.Vec2d
import javafx.animation.AnimationTimer
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Affine

private val INITIAL_ZOOM = 1.0


class DScene : BorderPane() {
    private val canvas = makeResizableCanvas()

    private val animationTimer = makeAnimationTimer()

    private val _nodes = mutableSetOf<DNode>()

    val nodes = _nodes

    var transform: Transform = Transform()
        set(value) {
            field = value
            emitTransformChanged(value)
        }

    val invertedTransform: Transform
        get() = transform.createInverse()

    private val emitTransformChanged = Emitter<Transform>()

    val onTransformChanged: Signal<Transform> = emitTransformChanged

    init {
        children.add(canvas)

        canvas.widthProperty().bind(widthProperty())
        canvas.heightProperty().bind(heightProperty())

        animationTimer.start()
    }

    private fun makeResizableCanvas(): ResizableCanvas = object : ResizableCanvas() {
        override fun draw() {
            draw(canvas.graphicsContext2D, Vec2d(canvas.width, canvas.height))
        }
    }

    private fun makeAnimationTimer(): AnimationTimer {
        val animationTimer = object : AnimationTimer() {
            override fun handle(t: Long) {
                draw(canvas.graphicsContext2D, Vec2d(canvas.width, canvas.height))
            }
        }
        return animationTimer
    }

    fun draw(gc: GraphicsContext, size: Vec2d) {
        gc.transform = Affine()
        gc.clearRect(0.0, 0.0, size.width, size.height)

        gc.transform = transform.toAffine()
        val nodes = nodes.sortedBy { it.z }
        nodes.forEach { it.draw(gc) }
    }

    fun addNode(node: DNode) {
        _nodes.add(node)
    }

    fun removeNode(node: DNode) {
        _nodes.remove(node)
    }
}
