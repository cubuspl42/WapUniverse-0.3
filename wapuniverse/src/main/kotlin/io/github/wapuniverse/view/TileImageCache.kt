package io.github.wapuniverse.view

import io.github.wapuniverse.core.ImageSetDatabase
import javafx.scene.image.Image

class TileImageCache(
        levelIndex: Int, tileImageSetName: String, imageSetDatabase: ImageSetDatabase, imageMap: ImageMap) {

    val cache: Array<Image?>

    init {
        val tileIndices = imageSetDatabase.listTiles(levelIndex, tileImageSetName)!!
        val max = tileIndices.max()!!
        cache = Array(max + 1, { i ->
            imageMap.findTileImage(levelIndex, tileImageSetName, i)
        })
    }

    fun getTileImage(tileIndex: Int): Image? {
        if (tileIndex >= 0 && tileIndex < cache.size) {
            return cache[tileIndex]
        } else return null
    }
}

