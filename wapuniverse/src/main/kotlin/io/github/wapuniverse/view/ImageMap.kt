package io.github.wapuniverse.view

import io.github.wapuniverse.core.ImageSetDatabase
import io.github.wapuniverse.utils.ResourceNotFound
import io.github.wapuniverse.utils.getResourceAsStream
import javafx.scene.image.Image
import java.util.*


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
    val rawImageMap = loadAllImagesRecursively(baseDirPath, "png")
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
