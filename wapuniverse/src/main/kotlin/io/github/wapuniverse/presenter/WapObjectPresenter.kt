package io.github.wapuniverse.presenter

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.WapObject
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.div
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.ImageNode
import io.github.wapuniverse.view.LayerNode
import io.github.wapuniverse.view.RectNode
import javafx.scene.paint.Color

class WapObjectPresenter(
        private val wapObject: WapObject,
        private val layerNode: LayerNode,
        private val worldPresenter: WorldPresenter
) : EntityPresenter {
    private var imageNode: ImageNode? = null
    private var rectNode: RectNode? = null

    private fun unpresent() {
        imageNode?.let { layerNode.removeImageNode(it) }
        rectNode?.let { layerNode.removeRectNode(it) }
    }

    private fun present() {
        val imageSetDatabase = worldPresenter.imageSetDatabase
        val imageMap = worldPresenter.imageMap
        val levelIndex = worldPresenter.world.levelIndex
        val imageSetId = wapObject.wwdObject.imageSet
        val frameIndex = wapObject.wwdObject.i
        val imageMd = imageSetDatabase.findObjectImageMetadata(levelIndex, imageSetId, frameIndex)
        val image = imageMap.findObjectImage(levelIndex, imageSetId, frameIndex)
        if(imageMd != null && image != null) {
            val imageSize = Vec2d(image.width, image.height).toVec2i()
            val minV: Vec2i = wapObject.position - imageSize / 2.0 + imageMd.offset.toVec2i()

            val imageNode = ImageNode(image, minV.x.toDouble(), minV.y.toDouble())
            layerNode.addImageNode(imageNode)
            this.imageNode = imageNode

            val rect = Rectangle2Di(minV.x, minV.y, imageSize.width, imageSize.height)
            val rectNode = RectNode(rect, Color.RED)
            layerNode.addRectNode(rectNode)
            this.rectNode = rectNode
        }
    }

    fun represent() {
        unpresent()
        present()
    }

    init {
        wapObject.changed.on { represent() }
        present()
    }
}
