package io.github.wapuniverse.editor

import io.github.wapuniverse.wap32.Wwd

data class World(
        val levelIndex: Int,
        val wwd: Wwd,
        val tileLayer: TileLayer,
        val entityComponent: EntityComponent
)