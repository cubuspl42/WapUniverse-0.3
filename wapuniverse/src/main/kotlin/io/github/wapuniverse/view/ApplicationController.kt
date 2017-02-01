package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import javafx.animation.AnimationTimer
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage


private val INITIAL_TITLE = "WapUniverse Editor"
private val INITIAL_WIDTH = 640.0
private val INITIAL_HEIGHT = 480.0

class ApplicationController(stage: Stage) {

    private val canvas = makeResizableCanvas()

    private val animationTimer = makeAnimationTimer()

    private val viewportVn = ViewportVn()

    init {
        val borderPane = BorderPane(canvas)

        stage.title = INITIAL_TITLE
        stage.scene = Scene(borderPane, INITIAL_WIDTH, INITIAL_HEIGHT)
        stage.show()

        canvas.widthProperty().bind(borderPane.widthProperty())
        canvas.heightProperty().bind(borderPane.heightProperty())

        canvas.setOnMouseMoved { ev -> viewportVn.onMouseMoved(ev) }
        canvas.setOnMouseDragged { ev -> viewportVn.onMouseDragged(ev) }
        canvas.setOnMousePressed { ev -> viewportVn.onMousePressed(ev) }
        canvas.setOnMouseReleased { ev -> viewportVn.onMouseReleased(ev) }
        canvas.setOnScroll { ev -> viewportVn.onScroll(ev) }

        animationTimer.start()
    }

    private fun makeResizableCanvas(): ResizableCanvas = object : ResizableCanvas() {
        override fun draw() {
            viewportVn.draw(canvas.graphicsContext2D, Vec2d(canvas.width, canvas.height))
        }
    }

    private fun makeAnimationTimer(): AnimationTimer {
        val animationTimer = object : AnimationTimer() {
            override fun handle(t: Long) {
                viewportVn.draw(canvas.graphicsContext2D, Vec2d(canvas.width, canvas.height))
            }
        }
        return animationTimer
    }
}
