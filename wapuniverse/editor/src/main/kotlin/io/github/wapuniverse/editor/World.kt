package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Signal
import io.github.wapuniverse.common.util.Rectangle2Di
import io.github.wapuniverse.common.util.Vec2i

interface World {
    val tilesChanged: Signal<Vec2i>

    val tilesBounds: Rectangle2Di

    val objectAdded: Signal<WObject>

    fun getTile(vt: Vec2i): Int

    fun setTile(vt: Vec2i, tileIdx: Int)

    fun addObject(): WObject
}
