package io.github.wapuniverse

import io.github.wapuniverse.util.Vec2i

private val INVISIBLE_TILE = -1

class Plane {
    val tiles = hashMapOf<Vec2i, Int>()

    fun getTile(v: Vec2i): Int {
        return tiles[v] ?: INVISIBLE_TILE
    }

    fun setTile(v: Vec2i, t: Int) {
        tiles[v] = t
    }
}
