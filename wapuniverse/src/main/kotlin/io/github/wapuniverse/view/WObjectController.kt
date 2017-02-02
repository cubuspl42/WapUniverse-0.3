package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.util.Vec2i
import io.github.wapuniverse.util.div
import io.github.wapuniverse.util.minus
import io.github.wapuniverse.wap32.WwdObject
import io.github.wapuniverse.world.WObject


private val NO_DRAW_ALPHA = 0.5

class WObjectController(
        wObject: WObject,
        private val scene: WvScene,
        private val imageMap: ImageMap,
        private val imageSetDatabase: ImageSetDatabase) {

    private var spriteNode: WvSpriteNode? = null

    init {
        wObject.wwdObjectChanged.on {
            update(it)
        }

        update(wObject.wwdObject)
    }

    private fun update(wwdObject: WwdObject) {
        val levelIndex = 1 // FIXME

        spriteNode?.let {
            scene.removeNode(it)
            spriteNode = null
        }

        imageMap.findObjectImage(levelIndex, wwdObject.imageSet, wwdObject.i)?.let { objImg ->
            // FIXME: imageMap/imageSetDatabase
            val imgMd = imageSetDatabase.findObjectImageMetadata(levelIndex, wwdObject.imageSet, wwdObject.i)!!
            val halfSize = Vec2d(objImg.width, objImg.height) / 2.0
            val anchor = halfSize - imgMd.offset
            val newSpriteNode = WvSpriteNode(objImg, anchor)
            newSpriteNode.position = Vec2i(wwdObject.x, wwdObject.y).toVec2d()
            newSpriteNode.alpha = if (wwdObject.drawFlags.noDraw) NO_DRAW_ALPHA else 1.0
            newSpriteNode.scale.x = if (wwdObject.drawFlags.mirror) -1.0 else 1.0
            newSpriteNode.scale.y = if (wwdObject.drawFlags.invert) -1.0 else 1.0

            spriteNode = newSpriteNode
            scene.addNode(newSpriteNode)
        }
    }
}
