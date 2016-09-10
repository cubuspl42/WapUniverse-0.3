package io.github.wapuniverse.controller

import com.google.common.io.Files
import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.*
import io.github.wapuniverse.lsd.scriptMetaMap
import io.github.wapuniverse.presenter.SmartObjectPresenter
import io.github.wapuniverse.presenter.WapObjectPresenter
import io.github.wapuniverse.utils.*
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

    private val primaryLayer = world.primaryLayer

    // Services

    private val entityService = EntityService(world.primaryLayer)

    // Controllers

    private val sceneInputController = SceneInputController()

    // View

    private val sBoxComponent = SBoxComponent()

    private val sceneView = SceneView(world.levelIndex, sceneCanvas, imageSetDatabase, imageMap, primaryLayer)

    //

    private var isDragged = false

    private var dragAnchor = Vec2d(0.0, 0.0)

    fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    init {
        SmartObjectPresenter(world.primaryLayer, sceneView, sceneInputController, sBoxComponent, sceneCanvas)
        WapObjectPresenter(world.primaryLayer, sceneView, imageSetDatabase, imageMap, world.levelIndex)

        sceneInputController.addInputHandler(MainInputHandler(sceneCanvas, sBoxComponent, sceneView))

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
            val script = when (ev.text) {
                "1" -> "Block"
                "2" -> "Ladder"
                "3" -> "Spikes"
                "4" -> "Column"
                "5" -> "Platform"
                else -> null
            }
            if (script != null) {
                val spawnOffset = Vec2i(128, 128)
                val repr = AdaptiveEntityRepr(
                        scriptId = script,
                        position = sceneView.cameraOffset.toVec2i() + spawnOffset
                )
                world.primaryLayer.addEntity(repr)
            }
        }

        val at = object : AnimationTimer() {
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
            fileChooser.extensionFilters.add(ExtensionFilter("WapWorld files", "*.wwd"))
            val selectedFile = fileChooser.showSaveDialog(sceneCanvas.scene.window)
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
