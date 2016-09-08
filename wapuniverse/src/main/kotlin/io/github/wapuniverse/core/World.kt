package io.github.wapuniverse.core

import io.github.wapuniverse.wap32.Wwd

class World(
        val levelIndex: Int,
        val wwd: Wwd,
        alphaTileMapper: AlphaTileMapper,
        imageSet: String
) {
    val primaryLayerImpl = LayerImpl(alphaTileMapper, imageSet)
    val primaryLayer: MutableLayer = primaryLayerImpl
}