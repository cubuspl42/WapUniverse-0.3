package io.github.wapuniverse.core

import io.github.wapuniverse.wap32.Wwd

class World(
        val levelIndex: Int,
        val wwd: Wwd,
        val tileLayer: TileLayer
) {
    val primaryLayer: MutableLayer = makeLayer()
}