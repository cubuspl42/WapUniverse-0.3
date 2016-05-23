package io.github.wapuniverse.view

import io.github.wapuniverse.editor.ImageSetDatabase
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
        try {
            val imageMd = imageSetDatabase.findObjectImageMetadata(levelIndex, imageSetId, frameIndex)
            return findImage(imageMd.path)
        } catch(e: ImageSetDatabase.ImageNotFound) {
            return null
        }
    }

    fun findTileImage(levelIndex: Int, tileImageSetName: String, tileIndex: Int): Image? {
        try {
            val tileMd = imageSetDatabase.findTileMetadata(levelIndex, tileImageSetName, tileIndex)
            return findImage(tileMd.path)
        } catch(e: ImageSetDatabase.ImageNotFound) {
            return null
        }
    }
}

fun loadImageMapFromPath(imageSetDatabase: ImageSetDatabase, baseDirPath: String): ImageMap {
    val rawImageMap = loadAllImagesRecursively(baseDirPath, "png")
    return ImageMap(imageSetDatabase, rawImageMap)
}
