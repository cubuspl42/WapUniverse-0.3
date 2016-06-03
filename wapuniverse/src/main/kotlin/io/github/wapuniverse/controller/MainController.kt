package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.EventHandlingStatus
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.view.SceneItem
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.view.loadImageMapFromPath
import javafx.animation.AnimationTimer
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import java.util.*
import kotlin.concurrent.schedule


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

private fun addAlphaTileMatrix(tileLayer: TileLayer, m: Int, n: Int, x: Int, y: Int, vararg tiles: AlphaTile) {
    val mx = makeMatrix(m, n, tiles.asList())
    val a = AlphaTileMatrix()
    a.offset = Vec2i(x, y)
    a.alphaTiles = mx
    tileLayer.addMatrix(a)
}

class MainController(root: Node, private val sceneCanvas: Canvas) {
    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromPath(imageSetDatabase, IMAGE_BASE_DIR_PATH)

    private val alphaTileMapper = AlphaTileMapper(level1FormulaGroup)

    private val tileLayer = TileLayer(alphaTileMapper, "ACTION")

    private val sceneView = SceneView(sceneCanvas, imageSetDatabase, imageMap, tileLayer)

    private val smartObjectComponent = SmartObjectComponent()

    private val editorObjectComponent = EditorObjectComponent()

    private val sceneInputController = SceneInputController()

    private var isDragged = false

    private var dragAnchor = Vec2d(0.0, 0.0)

    fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    init {
        SmartObjectPresenter(smartObjectComponent, editorObjectComponent, sceneView, sceneInputController, root)

        sceneInputController.addInputHandler(MainInputHandler(root, editorObjectComponent, sceneView))

        sceneCanvas.setOnMousePressed { ev ->
            if (ev.button == MouseButton.PRIMARY) {
                sceneInputController.onMousePressed(ev)
            } else if (ev.button == MouseButton.SECONDARY) {
                isDragged = true
                dragAnchor = invTr(ev.x, ev.y)
            }
        }

        sceneCanvas.setOnMouseReleased { ev ->
            if (ev.button == MouseButton.PRIMARY) {
                sceneInputController.onMouseReleased(ev)
            } else if (ev.button == MouseButton.SECONDARY) {
                isDragged = false
            }
        }

        sceneCanvas.setOnMouseDragged { ev ->
            if (ev.isSecondaryButtonDown) {
                val mv = Vec2d(ev.x, ev.y)
                sceneView.cameraOffset = dragAnchor - (mv * sceneView.scale)
            } else if (ev.isPrimaryButtonDown) {
                sceneInputController.onMouseDragged(ev)
            }
        }

        sceneCanvas.setOnMouseMoved { ev ->
            sceneInputController.onMouseMoved(ev)
        }

        sceneCanvas.setOnKeyPressed { ev ->
            val script = when (ev.text) {
                "1" -> blockScript
                "2" -> ladderScript
                "3" -> spikesScript
                "4" -> columnScript
                "5" -> platformScript
                else -> null
            }
            if (script != null) {
                smartObjectComponent.addSmartObject(SmartObject(tileLayer, script))
            }
        }

        val at = object: AnimationTimer() {
            override fun handle(t: Long) {
                sceneView.render()
            }
        }
        at.start()
    }
}
