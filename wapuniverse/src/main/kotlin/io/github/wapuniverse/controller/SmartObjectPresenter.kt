package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.RubberBand
import io.github.wapuniverse.view.SceneView
import javafx.geometry.Rectangle2D
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
        private val smartObject: SmartObject) : EditorObject() {

    private val rubberBand: RubberBand

    init {
        val r = scaleUp(smartObject.rect)
        rubberBand = RubberBand(r)
        rubberBand.color = Color.LIGHTBLUE

        sceneView.addItem(rubberBand)

        rubberBand.resized.connect { r ->
            val sdr = scaleDown(r)
            if (sdr.width > 0 && sdr.height > 0) {
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
            smartObject.offset = value / tileWidth
        }

    override val boundingBox: Rectangle2Di
        get() = rubberBand.boundingBox.toRect2Di()

    override fun onSelected() {
        rubberBand.z = 100000.0
        rubberBand.color = Color.RED
        sceneView.activeItem = rubberBand
    }

    override fun onUnselected() {
        rubberBand.z = 0.0
        rubberBand.color = Color.LIGHTBLUE
    }
}

class SmartObjectPresenter(
        smartObjectComponent: SmartObjectComponent,
        editorObjectComponent: EditorObjectComponent,
        sceneView: SceneView) {

    init {
        smartObjectComponent.objectAdded.connect { obj ->
            val e = SmartEditorObject(sceneView, obj)
            editorObjectComponent.addEditorObject(e)
        }
    }
}
