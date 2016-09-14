package io.github.wapuniverse.presenter

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.*
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.div
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.*
import javafx.geometry.Rectangle2D
import javafx.scene.Node

class WapObjectVo(
        private val sceneView: SceneView,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap,
        private val levelIndex: Int,
        private val wapObject: WapObject,
        private val sBoxComponent: SBoxComponent
) {
    private var sBox: SBox? = null
    private var sceneImage: SceneImage? = null

    private fun update() {
        val wwdObject = wapObject.wwdObject
        val imageMd = imageSetDatabase.findObjectImageMetadata(levelIndex, wwdObject.imageSet, wwdObject.i)
        val image = imageMap.findObjectImage(levelIndex, wwdObject.imageSet, wwdObject.i)

        if (imageMd != null && image != null) {
            val imageSize = Vec2d(image.width, image.height).toVec2i()
            val offset: Vec2i = wapObject.position - imageSize / 2.0 + imageMd.offset.toVec2i()
            val minV = offset
            val maxV = minV + imageSize

            val rect = Rectangle2D(
                    minV.x.toDouble(), minV.y.toDouble(),
                    imageSize.x.toDouble(), imageSize.y.toDouble())

            if(sBox == null || sBox!!.boundingRect != rect) {
                sBox?.let { sBoxComponent.removeSBox(it) }
                val sb = SBox(wapObject, rect)
                sBoxComponent.addSBox(sb)
                sBox = sb
            }

            sBox?.let {
                it.boundingRect = rect
            }

            sceneImage?.let { sceneView.removeItem(it) }
            sceneImage = SceneImage(offset.toVec2d(), image)
            sceneView.addItem(sceneImage!!)
        }
    }

    init {
        wapObject.changed.on {
            update()
        }

        update()
    }
}

class WapObjectPresenter(
        layer: Layer,
        private val sceneView: SceneView,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap,
        private val levelIndex: Int,
        private val sBoxComponent: SBoxComponent) {

    init {
        layer.entities.forEach { ent ->
            presentEntity(ent)
        }

        layer.onEntityAdded.on { ent ->
            presentEntity(ent)
        }
    }

    private fun presentEntity(ent: Entity) {
        if (ent is WapObject) {
            WapObjectVo(sceneView, imageSetDatabase, imageMap, levelIndex, ent, sBoxComponent)
        }
    }
}
