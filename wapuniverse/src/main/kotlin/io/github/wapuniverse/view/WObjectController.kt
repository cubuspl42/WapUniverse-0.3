package io.github.wapuniverse.view

import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.common.util.*
import io.github.wapuniverse.common.wap32.WwdObject
import io.github.wapuniverse.editor.WObject


private val NO_DRAW_ALPHA = 0.5

class WObjectController(
        wObject: WObject,
        private val scene: DScene,
        private val imageMap: ImageMap,
        private val imageSetDatabase: ImageSetDatabase) {

    private var spriteNode: DSpriteNode? = null

    init {
        wObject.onWwdObjectChanged.connect {
            update(wObject)
        }

        wObject.preRemoved.connect {
            spriteNode?.let { scene.removeNode(it) }
        }

        update(wObject)
    }

    private fun update(wObject: WObject) {
        val wwdObject = wObject.wwdObject
        val levelIndex = CFG_LEVEL_INDEX // FIXME

        spriteNode?.let {
            scene.removeNode(it)
            spriteNode = null
        }

        imageMap.findObjectImage(levelIndex, wwdObject.imageSet, wwdObject.i)?.let { objImg ->
            // FIXME: imageMap/imageSetDatabase
            val imgMd = imageSetDatabase.findObjectImageMetadata(levelIndex, wwdObject.imageSet, wwdObject.i)!!
            val objSize = Vec2d(objImg.width, objImg.height)
            val halfSize = objSize / 2.0
            val anchor = halfSize - imgMd.offset
            val newSpriteNode = DSpriteNode(objImg, anchor)
            val objPos = Vec2i(wwdObject.x, wwdObject.y).toVec2d()
            newSpriteNode.position = objPos
            newSpriteNode.alpha = if (wwdObject.drawFlags.noDraw) NO_DRAW_ALPHA else 1.0
            val sx = if (wwdObject.drawFlags.mirror) -1.0 else 1.0
            val sy =  if (wwdObject.drawFlags.invert) -1.0 else 1.0
            newSpriteNode.scale = Vec2d(sx, sy)

            spriteNode = newSpriteNode
            scene.addNode(newSpriteNode)
        }
    }
}
