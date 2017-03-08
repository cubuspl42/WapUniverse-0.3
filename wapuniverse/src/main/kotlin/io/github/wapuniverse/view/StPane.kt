package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.*
import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.w3c.dom.css.Rect
import java.util.logging.Logger

class StPane(private val dScene: DScene) : Pane() {
    private val log = Logger.getLogger(this.javaClass.name)

    private val nodesSet = hashSetOf<StNode>()

    private val nodesGroup = Group()

    private val selectionAreaRectNode = Rectangle()

    private var selectionAreaDiagonal: LineSegment2d? = null
        set(value) {
            field = value
            updateSelection(value)
            updateAreaSelectionRect(value)
        }

    val nodes: Set<StNode> = nodesSet

    val selectedNodes: Set<StNode>
        get() = nodes.filter { it.isSelected }.toSet()

    init {
        children.add(nodesGroup)
        children.add(selectionAreaRectNode)

        selectionAreaRectNode.fill = Color.rgb(255, 0, 0, 0.1)
        selectionAreaRectNode.stroke = Color.RED

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
            updateRectNodes()
            updateAreaSelectionRect(selectionAreaDiagonal)
        }
    }

    fun addNode(node: StNode): Boolean {
        val wasAdded = nodesSet.add(node)
        if (wasAdded) {
            node.dScene = dScene
            nodesGroup.children.add(node.rectNode)
            node.updateRectNode()
        }
        return wasAdded
    }

    fun removeNode(node: StNode): Boolean {
        val wasRemoved = nodesSet.remove(node)
        if (wasRemoved) {
            node.dScene = null
            nodesGroup.children.remove(node.rectNode)
        }
        return wasRemoved
    }

    private fun unselectAllNodes() {
        nodesSet.forEach { it.isSelected = false }
    }

    private fun selectNodes(rect: Rect2d) {
        unselectAllNodes()
        val nodesAt = nodesSet.filter { it.bounds.intersects(rect) }
        nodesAt.forEach { it.isSelected = true }
    }

    private fun updateRectNodes() {
        nodesSet.forEach { it.updateRectNode() }
    }

    private fun updateSelection(diagonal: LineSegment2d?) {
        when {
            diagonal != null -> {
                selectNodes(Rect2d.fromDiagonal(diagonal))
            }
            else -> {
                selectionAreaRectNode.isVisible = false
            }
        }
    }

    private fun updateAreaSelectionRect(diagonal: LineSegment2d?) {
        when {
            diagonal != null -> {
                val rect = Rect2d.fromDiagonal(diagonal)
                val viewRect = dScene.transform.map(rect)
                selectionAreaRectNode.rect = viewRect
                selectionAreaRectNode.isVisible = true
            }
            else -> {
                selectionAreaRectNode.isVisible = false
            }
        }
    }
}

class StNode {
    var bounds: Rect2d = Rect2d()
        set(value) {
            field = value
            updateRectNode()
        }

    internal var dScene: DScene? = null

    internal var rectNode = Rectangle()

    init {
        rectNode.fill = null
        updateStyle()
    }

    internal fun updateRectNode() {
        dScene?.let {
            rectNode.rect = it.transform.map(bounds)
        }
    }

    var isSelected: Boolean = false
        internal set(value) {
            field = value
            updateStyle()
        }

    private fun updateStyle() {
        when {
            isSelected -> rectNode.stroke = Color.ORANGE
            else -> rectNode.stroke = Color.LIGHTGRAY
        }
    }
}
