package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.layout.Region

class CanvasPane : Pane() {
    val canvas = Canvas()

//    lateinit var drawFn: (gc: GraphicsContext, width: Double, height: Double) -> Unit

    init {
        canvas.width = 640.0
        canvas.height = 480.0
        children.add(canvas)
        canvas.widthProperty().addListener { observable -> draw() }
        canvas.heightProperty().addListener { observable -> draw() }
    }

    override fun layoutChildren() {
        super.layoutChildren()
        val width = getWidth()
        val height = getHeight()
        val insets = getInsets()
        val contentX = insets.getLeft()
        val contentY = insets.getTop()
        val contentWith = Math.max(0.0, width - (insets.getLeft() + insets.getRight()))
        val contentHeight = Math.max(0.0, height - (insets.getTop() + insets.getBottom()))
        canvas.relocate(contentX, contentY)
        canvas.setWidth(contentWith)
        canvas.setHeight(contentHeight)
    }

    private fun draw() {
//        val gc = canvas.getGraphicsContext2D()
//        val width = canvas.getWidth()
//        val height = canvas.getHeight()
//        drawFn(gc, width, height)
    }
}
