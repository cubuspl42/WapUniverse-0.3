package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.util.Rectangle2Di
import io.github.wapuniverse.common.util.Vec2i

private val INVISIBLE_TILE_INDEX = -2 // FIXME

class WorldImpl : World {
    private val tiles = mutableMapOf<Vec2i, Int>()

    override val tilesChanged = Emitter<Vec2i>()

    override var tilesBounds = Rectangle2Di()
        private set

    override val objects = mutableSetOf<WObject>()

    override val objectAdded = Emitter<WObject>()

    override fun getTile(vt: Vec2i): Int {
        return tiles[vt] ?: INVISIBLE_TILE_INDEX
    }

    override fun setTile(vt: Vec2i, tileIdx: Int) {
        tiles[vt] = tileIdx
        tilesBounds = tilesBounds.union(vt)
        tilesChanged(vt)
    }

    override fun addObject(): WObject {
        val obj = WObjectImpl()
        objects.add(obj)
        objectAdded(obj)
        return obj
    }
}
