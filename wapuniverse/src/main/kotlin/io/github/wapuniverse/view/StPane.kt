package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.*
import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.util.logging.Logger

class StPane(private val dScene: DScene) : Pane() {
    private val log = Logger.getLogger(this.javaClass.name)

    private val nodesSet = hashSetOf<StNode>()

    private val nodesGroup = Group()

    private val selectionAreaRect = Rectangle()

    private var selectionAreaDiagonal: LineSegment2d? = null
        set(value) {
            field = value
            updateAreaSelectionRect(value)
        }

    val nodes: Set<StNode> = nodesSet

    val selectedNodes: Set<StNode>
        get() = nodes.filter { it.isSelected }.toSet()

    init {
        children.add(nodesGroup)
        children.add(selectionAreaRect)

        selectionAreaRect.fill = Color.rgb(255, 0, 0, 0.1)
        selectionAreaRect.stroke = Color.RED

        setOnMousePressed { ev ->
            val a = dScene.invertedTransform.map(ev.position)
            selectionAreaDiagonal = LineSegment2d(a, a)
        }

        setOnMouseDragged { ev ->
            val b = dScene.invertedTransform.map(ev.position)
            selectionAreaDiagonal = selectionAreaDiagonal?.copy(b = b)
        }

        setOnMouseReleased {
            selectionAreaDiagonal = null
        }

        dScene.onTransformChanged.connect {
            updateAreaSelectionRect(selectionAreaDiagonal)
        }
    }

    fun addNode(node: StNode): Boolean {
        val wasAdded = nodesSet.add(node)
        if (wasAdded) {
            nodesGroup.children.add(node)
        }
        return wasAdded
    }

    fun removeNode(node: StNode): Boolean {
        val wasRemoved = nodesSet.remove(node)
        if (wasRemoved) {
            nodesGroup.children.remove(node)
        }
        return wasRemoved
    }

    private fun selectNodesAt(p: Vec2d) {
        nodesSet.forEach { it.isSelected = false }
        val nodesAt = nodesSet.filter { it.bounds.contains(p) }
        nodesAt.forEach { it.isSelected = true }
    }

    private fun updateAreaSelectionRect(diagonal: LineSegment2d?) {
        when {
            diagonal != null -> {
                val rect = Rect2d.fromDiagonal(diagonal)
                val viewRect = dScene.transform.map(rect)
                selectionAreaRect.rect = viewRect
                selectionAreaRect.isVisible = true
            }
            else -> {
                selectionAreaRect.isVisible = false
            }
        }
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
