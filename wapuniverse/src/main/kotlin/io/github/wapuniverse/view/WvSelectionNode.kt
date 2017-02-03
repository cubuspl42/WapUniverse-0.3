package io.github.wapuniverse.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class WvSelectionNode : WvNode() {
    private val items = mutableSetOf<SnItemImpl>()

    fun addItem(): SnItem {
        val item = SnItemImpl()
        items.add(item)
        return item
    }

    override fun draw(gc: GraphicsContext) {
        items.forEach {
            val r = it.bbox.toRectangle2D()
            gc.stroke = Color.RED
            gc.strokeRect(r.minX, r.minY, r.width, r.height)
        }
    }
}
