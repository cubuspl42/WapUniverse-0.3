package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.Signal
import io.github.wapuniverse.common.util.height
import io.github.wapuniverse.common.util.minus
import io.github.wapuniverse.common.util.toVec2d
import io.github.wapuniverse.common.util.width
import javafx.animation.AnimationTimer
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.BorderPane
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform

private val INITIAL_ZOOM = 1.0


class DScene : BorderPane() {
    private val canvas = makeResizableCanvas()

    private val animationTimer = makeAnimationTimer()

    private var screen2world = Affine()

    private var world2screen = Affine()

    private var cameraOffset = Vec2d()
        set(value) {
            updateTransform(value)
            field = value
        }

    private var anchorPositionW: Vec2d? = null

    private var cameraZoom: Double = INITIAL_ZOOM
        set(value) {
            field = value
            updateTransform(cameraOffset)
        }

    private val _nodes = mutableSetOf<DNode>()

    val nodes = _nodes

    val transform: Transform
        get() = world2screen

    val invertedTransform: Transform
        get() = screen2world

    private val emitTransformChanged = Emitter<Transform>()

    val onTransformChanged: Signal<Transform> = emitTransformChanged

    init {
        children.add(canvas)

        updateTransform(cameraOffset)

        canvas.widthProperty().bind(widthProperty())
        canvas.heightProperty().bind(heightProperty())

        canvas.setOnMouseMoved { ev -> onMouseMoved(ev) }
        canvas.setOnMouseDragged { ev -> onMouseDragged(ev) }
        canvas.setOnMousePressed { ev -> onMousePressed(ev) }
        canvas.setOnMouseReleased { ev -> onMouseReleased(ev) }
        canvas.setOnScroll { ev -> onScroll(ev) }

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

    private fun updateTransform(newCameraOffset: Vec2d) {
        world2screen = Affine()
        world2screen.appendScale(cameraZoom, cameraZoom)
        world2screen.appendTranslation(-newCameraOffset.x, -newCameraOffset.y)
        screen2world = world2screen.createInverse()
        emitTransformChanged(world2screen)
    }

    private fun updateCameraOffset(anchorPositionW: Vec2d, zoomCenter: Vec2d) {
        val w0 = screen2world.transform(0.0, 0.0).toVec2d()
        val wv = screen2world.transform(zoomCenter.x, zoomCenter.y).toVec2d()
        val r = wv - w0
        cameraOffset = anchorPositionW - r
    }

    fun onMouseMoved(ev: MouseEvent) {
        val wv = screen2world.transform(ev.x, ev.y).toVec2d()
    }

    fun onMouseDragged(ev: MouseEvent) {
        if (ev.isSecondaryButtonDown) updateCameraOffset(anchorPositionW!!, Vec2d(ev.x, ev.y))
        onMouseMoved(ev)
    }

    fun onMousePressed(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.SECONDARY -> anchorPositionW = screen2world.transform(ev.x, ev.y).toVec2d()
            else -> Unit
        }
    }

    fun onMouseReleased(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.SECONDARY -> anchorPositionW = null
            else -> Unit
        }
    }

    fun onScroll(ev: ScrollEvent) {
        val anchorPositionW = anchorPositionW ?: screen2world.transform(ev.x, ev.y).toVec2d()
        val m = if (ev.deltaY > 0) 2.0 else 0.5
        val newCameraZoom = m * cameraZoom
        if (newCameraZoom in (0.005..10.0)) {
            cameraZoom = newCameraZoom
            updateCameraOffset(anchorPositionW, Vec2d(ev.x, ev.y))
        }
    }

    fun draw(gc: GraphicsContext, size: Vec2d) {
        gc.transform = Affine()
        gc.clearRect(0.0, 0.0, size.width, size.height)

        gc.transform = world2screen
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
