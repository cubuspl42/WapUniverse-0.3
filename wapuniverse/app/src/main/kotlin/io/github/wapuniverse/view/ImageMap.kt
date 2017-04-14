package io.github.wapuniverse.view

import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.common.util.*
import io.github.wapuniverse.editor.WObject
import javafx.scene.image.Image


private val pidExt = "PID"
private val pngExt = "png"

class ImageMap(
        private val imageSetDatabase: ImageSetDatabase,
        private val rawImageMap: Map<String, Image>
) {
    private fun findImage(pidImagePath: String): Image? {
        val pngImagePath = pidImagePath.replace(pidExt, pngExt)
        val image = rawImageMap[pngImagePath]
        return image
    }

    fun findObjectImage(levelIndex: Int, imageSetId: String, frameIndex: Int): Image? {
        val imageMd = imageSetDatabase.findObjectImageMetadata(levelIndex, imageSetId, frameIndex) ?: return null
        return findImage(imageMd.path)
    }

    fun findTileImage(levelIndex: Int, tileImageSetName: String, tileIndex: Int): Image? {
        val tileMd = imageSetDatabase.findTileMetadata(levelIndex, tileImageSetName, tileIndex) ?: return null
        return findImage(tileMd.path)
    }
}

fun loadImageMapFromPath(imageSetDatabase: ImageSetDatabase, baseDirPath: String): ImageMap {
    val rawImageMap = loadAllImagesRecursively(baseDirPath, pngExt)
    return ImageMap(imageSetDatabase, rawImageMap)
}

fun loadImageMapFromResources(imageSetDatabase: ImageSetDatabase, prefix: String): ImageMap {
    val imageMetadataList = imageSetDatabase.readAllImageMetadata()
    val imagesDict = hashMapOf<String, Image>()
    imageMetadataList.map {
        val pngImagePath = it.path.replace(pidExt, pngExt)
        val resImagePath = prefix + pngImagePath
        try {
            getResourceAsStream(resImagePath).use {
                imagesDict.put(pngImagePath, Image(it))
            }
        } catch(e: ResourceNotFound) {
            System.err.println(e.message)
        }
    }
    return ImageMap(imageSetDatabase, imagesDict)
}

fun wObjectBounds(
        wObject: WObject, levelIndex: Int, imageSetDatabase: ImageSetDatabase, imageMap: ImageMap
): Rect2d? {
    val wwdObject = wObject.wwdObject
    imageMap.findObjectImage(levelIndex, wwdObject.imageSet, wwdObject.i)?.let { objImg ->
        val imgMd = imageSetDatabase.findObjectImageMetadata(levelIndex, wwdObject.imageSet, wwdObject.i)!!
        val objSize = Vec2d(objImg.width, objImg.height)
        val halfSize = objSize / 2.0
        val anchor = halfSize - imgMd.offset
        val objPos = Vec2i(wwdObject.x, wwdObject.y).toVec2d()
        val minV = objPos - anchor
        val bbox = Rect2d(minV.x, minV.y, objSize.x, objSize.y)
        return bbox
    }
    return null
}
