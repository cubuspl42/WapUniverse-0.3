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
        private val stPlane: StPlane,
        private val imageMap: ImageMap,
        private val imageSetDatabase: ImageSetDatabase) {

    private var spriteNode: DSpriteNode? = null

    private val stNode = StNode()

    init {
        wObject.wwdObjectChanged.connect {
            update(it)
        }

        stPlane.addNode(stNode)

        update(wObject.wwdObject)
    }

    private fun update(wwdObject: WwdObject) {
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
            newSpriteNode.scale.x = if (wwdObject.drawFlags.mirror) -1.0 else 1.0
            newSpriteNode.scale.y = if (wwdObject.drawFlags.invert) -1.0 else 1.0

            spriteNode = newSpriteNode
            scene.addNode(newSpriteNode)

            val minV = objPos - anchor
            val minVi = minV.toVec2i()
            val objSizei = objSize.toVec2i()
            val bbox = Rectangle2Di(minVi.x, minVi.y, objSizei.x, objSizei.y)
            stNode.bounds = bbox
        }
    }
}
