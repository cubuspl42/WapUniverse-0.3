package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.*
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.times
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.view.loadImageMapFromPath
import io.github.wapuniverse.wap32.Wwd
import javafx.animation.AnimationTimer
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

class WorldController(root: Node, private val sceneCanvas: Canvas, wwd: Wwd) {

    // Global resources

    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromPath(imageSetDatabase, IMAGE_BASE_DIR_PATH)

    // World loader

    private val worldLoader = WorldLoader()

    private val world = worldLoader.loadWorld(wwd)

    // World

    private val tileLayer = world.tileLayer

    private val entityComponent = world.entityComponent

    // Services

    private val entityService = EntityService(entityComponent)

    // Controllers

    private val sceneInputController = SceneInputController()

    // View

    private val sBoxComponent = SBoxComponent()

    private val sceneView = SceneView(world.levelIndex, sceneCanvas, imageSetDatabase, imageMap, tileLayer)

    //

    private var isDragged = false

    private var dragAnchor = Vec2d(0.0, 0.0)

    fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    init {
        SmartObjectPresenter(entityComponent, sceneView, sceneInputController, sBoxComponent, root)

        sceneInputController.addInputHandler(MainInputHandler(root, sBoxComponent, sceneView))

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
            if (ev.text == "a") {
                sBoxComponent.selectAll()
            }
            if (ev.code == KeyCode.DELETE) {
                val se = sBoxComponent.selectedEntities
                entityService.destroyEntities(se)
            }
//            val script = when (ev.text) {
//                "1" -> blockScript
//                "2" -> ladderScript
//                "3" -> spikesScript
//                "4" -> columnScript
//                "5" -> platformScript
//                else -> null
//            }
//            if (script != null) {
//                entityComponent.addEntity(SmartObject(tileLayer, script))
//            }
        }

        val at = object: AnimationTimer() {
            override fun handle(t: Long) {
                sceneView.render()
            }
        }
        at.start()
    }
}
