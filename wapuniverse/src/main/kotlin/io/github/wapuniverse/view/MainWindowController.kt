package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.Brush
import io.github.wapuniverse.BrushState
import io.github.wapuniverse.PxCanvas
import io.github.wapuniverse.util.*
import javafx.animation.AnimationTimer
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import java.net.URL
import java.util.*
import java.util.logging.Logger

private val T = 64.0

private fun world2tile(wv: Vec2d): Vec2i = (wv / T).floor().toVec2i()

class MainWindowController : Initializable {
    private val log = Logger.getLogger(this.javaClass.name)

    @FXML
    private lateinit var borderPane: BorderPane

    private lateinit var canvas: ResizableCanvas

    private val pxCanvas = PxCanvas()

    private var brush: Brush = Brush(pxCanvas)

    private var screen2world = Affine()

    private var world2screen = Affine()

    private var cameraOffset = Vec2d()
        set(value) {
            updateTransform(value)
            field = value
        }

    private fun updateTransform(newCameraOffset: Vec2d) {
        world2screen = Affine()
        world2screen.appendScale(cameraZoom, cameraZoom)
        world2screen.appendTranslation(-newCameraOffset.x, -newCameraOffset.y)
        screen2world = world2screen.createInverse()
    }

    private var anchorPositionW: Vec2d? = null

    private var cameraZoom: Double = 2.0
        set(value) {
            field = value
            updateTransform(cameraOffset)
        }

    init {
        updateTransform(cameraOffset)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        canvas = makeResizableCanvas()
        borderPane.children.add(canvas)
        canvas.widthProperty().bind(borderPane.widthProperty())
        canvas.heightProperty().bind(borderPane.heightProperty())
        canvas.setOnMouseMoved { ev -> onMouseMoved(ev) }
        canvas.setOnMouseDragged { ev -> onMouseDragged(ev) }
        canvas.setOnMousePressed { ev -> onMousePressed(ev) }
        canvas.setOnMouseReleased { ev -> onMouseReleased(ev) }
        canvas.setOnScroll { ev -> onScroll(ev) }

        (object : AnimationTimer() {
            override fun handle(t: Long) {
                _draw()
            }
        }).start()
    }

    private fun makeResizableCanvas() = object : ResizableCanvas() {
        override fun draw() {
            _draw()
        }
    }

    private fun _draw() {
        val gc = canvas.graphicsContext2D
        gc.transform = Affine()
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        gc.transform = world2screen

        pxCanvas.pixels.forEach { entry ->
            val (bpT, color) = entry
            val bp = bpT * T
            gc.fill = color
            gc.fillRect(bp.x.toDouble(), bp.y.toDouble(), T, T)
        }

        gc.fill = Color.BLUE
        val brp = brush.position.toVec2d() * T
        gc.fillRect(brp.x, brp.y, T, T)
    }

    @FXML
    private fun handleNewLevel(ev: ActionEvent) {

    }

    private fun onMouseMoved(ev: MouseEvent) {
        val wv = screen2world.transform(ev.x, ev.y).toVec2d()
        brush.position = world2tile(wv)
    }

    private fun onMouseDragged(ev: MouseEvent) {
        onMouseMoved(ev)
        if (ev.isSecondaryButtonDown) updateCameraOffset(anchorPositionW!!, Vec2d(ev.x, ev.y))
    }

    private fun updateCameraOffset(anchorPositionW: Vec2d, zoomCenter: Vec2d) {
        val w0 = screen2world.transform(0.0, 0.0).toVec2d()
        val wv = screen2world.transform(zoomCenter.x, zoomCenter.y).toVec2d()
        val r = wv - w0
        cameraOffset = anchorPositionW - r
    }

    private fun onMousePressed(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.PRIMARY -> brush.state = BrushState.PRESSED
            MouseButton.SECONDARY -> anchorPositionW = screen2world.transform(ev.x, ev.y).toVec2d()
            else -> Unit
        }
    }

    private fun onMouseReleased(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.PRIMARY -> brush.state = BrushState.RELEASED
            MouseButton.SECONDARY -> anchorPositionW = null
            else -> Unit
        }
    }

    private fun onScroll(ev: ScrollEvent) {
        val anchorPositionW = anchorPositionW ?: screen2world.transform(ev.x, ev.y).toVec2d()
        val m = if (ev.deltaY > 0) 2.0 else 0.5
        val newCameraZoom = m * cameraZoom
        if (newCameraZoom in (0.005..10.0)) {
            cameraZoom = newCameraZoom
            updateCameraOffset(anchorPositionW, Vec2d(ev.x, ev.y))
        }
    }
}
