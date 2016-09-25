package io.github.wapuniverse.presenter

import io.github.wapuniverse.core.ImageSetDatabase
import io.github.wapuniverse.core.Layer
import io.github.wapuniverse.core.World
import io.github.wapuniverse.core.WorldEditor
import io.github.wapuniverse.view.ImageMap
import io.github.wapuniverse.view.LayerNode
import io.github.wapuniverse.view.TileImageCache
import io.github.wapuniverse.view.WorldNode

class WorldPresenter(
        val world: World,
        private val worldEditor: WorldEditor,
        private val worldNode: WorldNode,
        val imageSetDatabase: ImageSetDatabase,
        val imageMap: ImageMap
) {
    private val primaryLayerPresenter = LayerPresenter(world.primaryLayer, makeLayerNode(world.primaryLayer), this)

    private fun makeLayerNode(layer: Layer): LayerNode {
        val layerNode = LayerNode(layer, worldEditor, makeTileImageCache(layer))
        worldNode.addLayerNode(layerNode)
        return layerNode
    }

    private fun makeTileImageCache(layer: Layer): TileImageCache {
        return TileImageCache(world.levelIndex, layer.imageSet, imageSetDatabase, imageMap)
    }
}