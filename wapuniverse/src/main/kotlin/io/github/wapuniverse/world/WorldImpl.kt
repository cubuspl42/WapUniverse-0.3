package io.github.wapuniverse.world

import io.github.wapuniverse.util.Rectangle2Di
import io.github.wapuniverse.util.Signal
import io.github.wapuniverse.util.Vec2i

private val INVISIBLE_TILE_INDEX = -2 // FIXME

class WorldImpl : World {
    private val tiles = mutableMapOf<Vec2i, Int>()

    override val tilesChanged = Signal<Vec2i>()

    override var tilesBounds = Rectangle2Di()
        private set

    private val objects = mutableSetOf<WObject>()

    override val objectAdded = Signal<WObject>()

    override fun getTile(vt: Vec2i): Int {
        return tiles[vt] ?: INVISIBLE_TILE_INDEX
    }

    override fun setTile(vt: Vec2i, tileIdx: Int) {
        tiles[vt] = tileIdx
        tilesBounds = tilesBounds.union(vt)
        tilesChanged._emit(vt)
    }

    override fun addObject(): WObject {
        val obj = WObjectImpl()
        objects.add(obj)
        objectAdded._emit(obj)
        return obj
    }
}
