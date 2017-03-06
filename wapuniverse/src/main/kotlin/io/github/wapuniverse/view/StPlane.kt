package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.Rect2d
import io.github.wapuniverse.common.util.Vec2d
import io.github.wapuniverse.common.util.Rectangle2Di
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class StPlane : Parent() {
    private val stNodes = hashSetOf<StNode>()

    init {
        setOnMouseClicked { ev ->
            selectNodesAt(Vec2d(ev.x, ev.y))
        }
    }

    fun addNode(node: StNode): Boolean {
        val wasAdded = stNodes.add(node)
        if (wasAdded) {
            children.add(node)
        }
        return wasAdded
    }

    fun removeNode(node: StNode): Boolean {
        val wasRemoved = stNodes.remove(node)
        if (wasRemoved) {
            children.remove(node)
        }
        return wasRemoved
    }

    private fun selectNodesAt(p: Vec2d) {
        stNodes.forEach { it.isSelected = false }
        val nodes = stNodes.filter { it.bounds.contains(p) }
        nodes.forEach { it.isSelected = true }
    }
}

class StNode : Rectangle() {
    private val STNODE_CLASS = "stnode"
    private val SELECTED_CLASS = "stnode-selected"

    init {
        styleClass.add(STNODE_CLASS)

        fill = Color.TRANSPARENT
        strokeWidth = 1.0

        updateStyle()
    }

    var bounds: Rect2d
        get() {
            return Rect2d(x, y, width, height)
        }
        set(value) {
            x = value.minX + 0.5
            y = value.minY + 0.5
            width = value.width.toDouble()
            height = value.height.toDouble()
        }

    var isSelected: Boolean = false
        internal set(value) {
            field = value
            updateStyle()
        }

    private fun updateStyle() {
        when {
            isSelected -> stroke = Color.RED
            else -> stroke = Color.LIGHTGRAY
        }
    }
}
