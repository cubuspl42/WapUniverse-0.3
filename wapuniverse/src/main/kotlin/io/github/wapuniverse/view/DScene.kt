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

    private var screen2world = Transform()

    private var world2screen = Transform()

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

        val clipRect = Rectangle()
        clipRect.widthProperty().bind(widthProperty())
        clipRect.heightProperty().bind(heightProperty())
        clip = clipRect

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
        world2screen = Transform(-newCameraOffset, cameraZoom)
        screen2world = world2screen.createInverse()
        emitTransformChanged(world2screen)
    }

    private fun updateCameraOffset(anchorPositionW: Vec2d, zoomCenter: Vec2d) {
        val w0 = screen2world.map(Vec2d())
        val wv = screen2world.map(zoomCenter)
        val r = wv - w0
        cameraOffset = anchorPositionW - r
    }

    fun onMouseMoved(ev: MouseEvent) {
        val wv = screen2world.map(ev.x, ev.y)
    }

    fun onMouseDragged(ev: MouseEvent) {
        if (ev.isSecondaryButtonDown) updateCameraOffset(anchorPositionW!!, Vec2d(ev.x, ev.y))
        onMouseMoved(ev)
    }

    fun onMousePressed(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.SECONDARY -> anchorPositionW = screen2world.map(ev.x, ev.y)
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
        val anchorPositionW = anchorPositionW ?: screen2world.map(ev.x, ev.y)
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

        gc.transform = world2screen.toAffine()
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
