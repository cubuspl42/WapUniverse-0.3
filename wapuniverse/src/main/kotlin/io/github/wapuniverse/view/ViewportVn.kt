package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.Brush
import io.github.wapuniverse.BrushState
import io.github.wapuniverse.PxCanvas
import io.github.wapuniverse.util.*
import io.github.wapuniverse.wap32.Wwd
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.paint.Color
import javafx.scene.transform.Affine

private val T = 64.0

private fun world2tile(wv: Vec2d): Vec2i = (wv / T).floor().toVec2i()

private val INITIAL_ZOOM = 1.0

class ViewportVn(private val wwd: Wwd, private val imageMap: ImageMap) {
    private val pxCanvas = PxCanvas()

    private var brush: Brush = Brush(pxCanvas)

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

    init {
        updateTransform(cameraOffset)
    }

    private fun updateTransform(newCameraOffset: Vec2d) {
        world2screen = Affine()
        world2screen.appendScale(cameraZoom, cameraZoom)
        world2screen.appendTranslation(-newCameraOffset.x, -newCameraOffset.y)
        screen2world = world2screen.createInverse()
    }

    fun onMouseMoved(ev: MouseEvent) {
        val wv = screen2world.transform(ev.x, ev.y).toVec2d()
        brush.position = world2tile(wv)
    }

    fun onMouseDragged(ev: MouseEvent) {
        onMouseMoved(ev)
        if (ev.isSecondaryButtonDown) updateCameraOffset(anchorPositionW!!, Vec2d(ev.x, ev.y))
    }

    fun onMousePressed(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.PRIMARY -> brush.state = BrushState.PRESSED
            MouseButton.SECONDARY -> anchorPositionW = screen2world.transform(ev.x, ev.y).toVec2d()
            else -> Unit
        }
    }

    fun onMouseReleased(ev: MouseEvent) {
        when (ev.button) {
            MouseButton.PRIMARY -> brush.state = BrushState.RELEASED
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

        val LEVEL_INDEX = 1
        val TILE_IMAGE_SET = "ACTION"

        val actionPlane = wwd.planes[1]
        for (i in (0..actionPlane.tilesHigh - 1)) {
            for (j in (0..actionPlane.tilesWide - 1)) {
                val t = actionPlane.getTile(i, j)
                if (t > 0) {
                    val tImg = imageMap.findTileImage(LEVEL_INDEX, TILE_IMAGE_SET, t)
                    gc.drawImage(tImg, j * T, i * T, T, T)
                }
            }
        }

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

    private fun updateCameraOffset(anchorPositionW: Vec2d, zoomCenter: Vec2d) {
        val w0 = screen2world.transform(0.0, 0.0).toVec2d()
        val wv = screen2world.transform(zoomCenter.x, zoomCenter.y).toVec2d()
        val r = wv - w0
        cameraOffset = anchorPositionW - r
    }
}