package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.*
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.wap32.loadWwd
import javafx.animation.AnimationTimer
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import java.io.FileInputStream
import java.util.*
import kotlin.concurrent.schedule


class MainController(private val rootNode: Node, private val sceneCanvas: Canvas) {
    private var worldController: WorldController? = null

    fun loadWorld(wwdPath: String) {
        FileInputStream(wwdPath).use {
            val wwd = loadWwd(it)
            worldController = WorldController(rootNode, sceneCanvas, wwd)
        }
    }
}
