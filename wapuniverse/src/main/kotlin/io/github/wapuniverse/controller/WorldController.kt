package io.github.wapuniverse.controller

import com.google.common.io.Files
import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.scriptMetaMap
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.times
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.view.ImageMap
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.dumpWwd
import javafx.animation.AnimationTimer
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import java.io.FileOutputStream

private val worldDumpPath = "/home/kuba/Dropbox/temp/LEVEL.wwd"

class WorldController(
        private val root: Node,
        private var wwdPath: String?,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap,
        private val sceneCanvas: Canvas,
        wwd: Wwd
) {
    // World loader

    private val worldLoader = WorldLoader()

    private val world = worldLoader.loadWorld(wwd)

    private val worldDumper = WorldDumper()

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
            when (ev.text) {
                "a" -> sBoxComponent.selectAll()
//                "s" -> dumpWorld()
                "s" -> save()
            }
            if (ev.code == KeyCode.DELETE) {
                val se = sBoxComponent.selectedEntities
                entityService.destroyEntities(se)
            }
            val scriptMap = scriptMetaMap[world.levelIndex]!!
            val script = when (ev.text) {
                "1" -> scriptMap["Block"]
                "2" -> scriptMap["Ladder"]
                "3" -> scriptMap["Spikes"]
                "4" -> scriptMap["Column"]
                "5" -> scriptMap["Platform"]
                else -> null
            }
            if (script != null) {
                entityComponent.addEntity(SmartObject(tileLayer, script))
            }
        }

        val at = object: AnimationTimer() {
            override fun handle(t: Long) {
                sceneView.render()
            }
        }
        at.start()
    }

    fun save() {
        if (wwdPath != null) {
            dumpWorld(wwdPath!!)
        } else {
            val fileChooser = FileChooser()
            fileChooser.title = "Open WWD File"
            fileChooser.extensionFilters.add(ExtensionFilter("Text Files", "*.wwd", "*.WWD"))
            val selectedFile = fileChooser.showSaveDialog(root.scene.window)
            if (selectedFile != null) {
                wwdPath = selectedFile.absolutePath
                dumpWorld(selectedFile.absolutePath)
            }
        }
    }

    fun dumpWorld(wwdPath: String) {
        val wwd = worldDumper.dumpWorld(world)
        FileOutputStream(wwdPath).use {
            dumpWwd(it, wwd)
        }
    }
}
