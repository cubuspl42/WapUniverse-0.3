package io.github.wapuniverse.world

import io.github.wapuniverse.util.Rectangle2Di
import io.github.wapuniverse.util.Signal
import io.github.wapuniverse.util.Vec2i

interface World {
    val tilesChanged: Signal<Vec2i>

    val tilesBounds: Rectangle2Di

    val objectAdded: Signal<WObject>

    fun getTile(vt: Vec2i): Int

    fun setTile(vt: Vec2i, tileIdx: Int)

    fun addObject(): WObject
}

fun makeWorld(): World = WorldImpl()