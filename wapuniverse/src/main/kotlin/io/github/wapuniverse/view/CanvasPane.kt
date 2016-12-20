package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import java.util.logging.Logger

abstract class CanvasPane : Pane() {
    val log = Logger.getLogger(this.javaClass.name)

    val canvas = Canvas()

    init {
        canvas.width = 640.0
        canvas.height = 480.0
        children.add(canvas)
        canvas.widthProperty().addListener { observable -> _draw() }
        canvas.heightProperty().addListener { observable -> _draw() }
    }

    override fun layoutChildren() {
        super.layoutChildren()
        val contentX = insets.left
        val contentY = insets.top
        val contentWith = Math.max(0.0, width - (insets.left + insets.right))
        val contentHeight = Math.max(0.0, height - (insets.top + insets.bottom))
        canvas.relocate(contentX, contentY)
        canvas.width = contentWith
        canvas.height = contentHeight
    }

    fun _draw() {
        val gc = canvas.graphicsContext2D
        val width = canvas.width
        val height = canvas.height
        gc.fill = Color.RED
        gc.stroke = Color.RED
        gc.lineTo(width, height)
        log.info("($width, $height)")
//        draw(gc, width, height)
    }
}
