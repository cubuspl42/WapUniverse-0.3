package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.RubberBand
import io.github.wapuniverse.view.SceneView
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.paint.Color
import java.util.*


fun scaleDown(r: Rectangle2D): Rectangle2Di {
    val minX = Math.round(r.minX / tileWidth).toInt()
    val minY = Math.round(r.minY / tileWidth).toInt()
    val maxX = Math.round(r.maxX / tileWidth).toInt()
    val maxY = Math.round(r.maxY / tileWidth).toInt()
    return Rectangle2Di(minX, minY, maxX - minX, maxY - minY)
}

fun scaleUp(r: Rectangle2Di): Rectangle2D {
    val minX = r.minX * tileWidth
    val minY = r.minY * tileWidth
    val maxX = r.maxX * tileWidth
    val maxY = r.maxY * tileWidth
    return Rectangle2D(minX, minY, maxX - minX, maxY - minY)
}

class SmartEditorObject(
        private val sceneView: SceneView,
        private val sceneInputController: SceneInputController,
        private val smartObject: SmartObject,
        private val root: Node) : EditorObject() {

    private val rubberBand: RubberBand

    private fun updateColor() {
        rubberBand.color = when {
            isSelected -> Color.RED
            isHovered -> Color.BLUE
            else -> Color.LIGHTBLUE
        }
    }

    init {
        val r = scaleUp(smartObject.rect)
        rubberBand = RubberBand(r, root)
        updateColor()

        sceneView.addItem(rubberBand)

        rubberBand.resized.connect { r ->
            val sdr = scaleDown(r)
            if (sdr.width > 0 && sdr.height > 0) {
                smartObject.offset = Vec2i(sdr.minX, sdr.minY)
                smartObject.resize(sdr.width, sdr.height)
            }
        }

        smartObject.changed.connect {
            val sur = scaleUp(smartObject.rect)
            rubberBand.offset = Vec2d(sur.minX, sur.minY)
            rubberBand.width = sur.width
            rubberBand.height = sur.height
        }
    }

    override var position: Vec2i
        get() = smartObject.offset * tileWidth
        set(value) {
            val p = value.toVec2d() / tileWidth
            val rp = Vec2i(Math.round(p.x).toInt(), Math.round(p.y).toInt())
            smartObject.offset = rp
        }

    override val boundingBox: Rectangle2Di
        get() = rubberBand.boundingBox.toRect2Di()

    override fun onSelected() {
        rubberBand.z = 100000.0
        updateColor()
        sceneInputController.addInputHandler(rubberBand)
    }

    override fun onUnselected() {
        rubberBand.z = 0.0
        updateColor()
        sceneInputController.removeInputHandler(rubberBand)
    }

    override fun onHover() {
        updateColor()
    }

    override fun onUnhover() {
        updateColor()
    }
}

class SmartObjectPresenter(
        smartObjectComponent: SmartObjectComponent,
        editorObjectComponent: EditorObjectComponent,
        sceneView: SceneView,
        sceneInputController: SceneInputController,
        root: Node) {

    init {
        smartObjectComponent.objectAdded.connect { obj ->
            val e = SmartEditorObject(sceneView, sceneInputController, obj, root)
            editorObjectComponent.addEditorObject(e)
        }
    }
}
