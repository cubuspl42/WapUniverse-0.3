package io.github.wapuniverse.presenter

import io.github.wapuniverse.core.AdaptiveEntity
import io.github.wapuniverse.core.tileWidth
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.view.LayerNode
import io.github.wapuniverse.view.RectNode
import javafx.scene.paint.Color

class AdaptiveEntityPresenter(
        private val adaptiveEntity: AdaptiveEntity,
        private val layerNode: LayerNode
) : EntityPresenter {
    private var rectNode: RectNode? = null

    private fun unpresent() {
        rectNode?.let { layerNode.removeRectNode(it) }
    }

    private fun present() {
        val ri = adaptiveEntity.rect
        val tw = tileWidth.toInt()
        val rect = Rectangle2Di(ri.minX * tw, ri.minY * tw, ri.width * tw, ri.height * tw)
        val rectNode = RectNode(rect, Color.ORANGE)
        layerNode.addRectNode(rectNode)
        this.rectNode = rectNode
    }

    fun represent() {
        unpresent()
        present()
    }

    init {
        adaptiveEntity.changed.on { represent() }
        present()
    }
}
