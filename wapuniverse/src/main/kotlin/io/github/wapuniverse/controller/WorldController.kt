package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.*
import io.github.wapuniverse.presenter.WorldPresenter
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.ImageMap
import io.github.wapuniverse.view.WorldNode
import io.github.wapuniverse.view.makeTransforms
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdObject
import io.github.wapuniverse.wap32.dumpWwd
import javafx.animation.AnimationTimer
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

    private val world = loadWorld(wwd)

    private val worldEditor = WorldEditor(world)

    // World

    private val primaryLayer = world.primaryLayer

    // Controllers

    private val worldNode = WorldNode(world)

    private val worldPresenter = WorldPresenter(world, worldEditor, worldNode, imageSetDatabase, imageMap)

    private var selectionController: SelectionController? = null

    private var entitySelection: EntitySelection? = null

    private var isDragged = false

    private var dragAnchor = Vec2d(0.0, 0.0)

    fun invTr(x: Double, y: Double): Vec2d {
        val ap = makeTransforms(worldEditor.cameraOffset, worldEditor.cameraZoom)
        return ap.invTransform.transform(x, y).toVec2d()
    }

    init {
        sceneCanvas.setOnMousePressed { ev ->
            if (ev.button == MouseButton.PRIMARY) {
                entitySelection = worldEditor.startSelection(invTr(ev.x, ev.y).toVec2i())
            } else if (ev.button == MouseButton.SECONDARY) {
                isDragged = true
                dragAnchor = invTr(ev.x, ev.y)
            }
        }

        sceneCanvas.setOnMouseReleased { ev ->
            if (ev.button == MouseButton.PRIMARY) {
                entitySelection?.let {
                    it.commit()
                    entitySelection = null
                }
            } else if (ev.button == MouseButton.SECONDARY) {
                isDragged = false
            }
        }

        sceneCanvas.setOnMouseDragged { ev ->
            if (ev.isSecondaryButtonDown) {
                val mv = Vec2d(ev.x, ev.y)
                worldEditor.cameraOffset = dragAnchor - (mv * worldEditor.cameraZoom)
            } else if (ev.isPrimaryButtonDown) {
                entitySelection?.let {
                    val minV = it.area.minV
                    val maxV = invTr(ev.x, ev.y)
                    val w = Math.max(maxV.x - minV.x, 0.0)
                    val h = Math.max(maxV.y - minV.y, 0.0)
                    val rect = Rectangle2Di(minV.x, minV.y, w.toInt(), h.toInt())
                    it.updateArea(rect)
                }
            }
        }

        sceneCanvas.setOnMouseMoved { ev ->
        }

        sceneCanvas.setOnKeyPressed { ev ->
            when (ev.text) {
                "s" -> save()
                "w" -> {
                    primaryLayer.addEntity(WapObjectRepr(WwdObject(
                            id = -1,
                            logic = "Officer",
                            imageSet = "LEVEL_OFFICER",
                            x = 32,
                            y = 32,
                            i = -1
                    )))
                }
            }
            if (ev.code == KeyCode.DELETE) {
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
                        position = worldEditor.cameraOffset.toVec2i() + spawnOffset
                )
                world.primaryLayer.addEntity(repr)
            }
        }

        val at = object : AnimationTimer() {
            override fun handle(t: Long) {
                draw()
            }
        }
        at.start()
    }

    fun draw() {
        val viewport = Vec2i(sceneCanvas.width.toInt(), sceneCanvas.height.toInt())
        worldNode.draw(sceneCanvas.graphicsContext2D, worldEditor.cameraOffset, worldEditor.cameraZoom, viewport)
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
        val wwd = dumpWorld(world)
        FileOutputStream(wwdPath).use {
            dumpWwd(it, wwd)
        }
    }
}
