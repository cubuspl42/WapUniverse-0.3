package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.util.Rectangle2Di
import io.github.wapuniverse.common.util.Vec2i
import io.github.wapuniverse.common.wap32.Wwd

private val INVISIBLE_TILE_INDEX = -2 // FIXME

class WorldImpl(wwd: Wwd) : World {
    private val tiles = mutableMapOf<Vec2i, Int>()

    override val tilesChanged = Emitter<Vec2i>()

    override var tilesBounds = Rectangle2Di()
        private set

    override val objects = mutableSetOf<WObject>()

    override val onObjectAdded = Emitter<WObject>()

    init {
        val actionPlane = wwd.planes[1] // FIXME

        for (i in (0..actionPlane.tilesHigh - 1)) {
            for (j in (0..actionPlane.tilesWide - 1)) {
                val tileIdx = actionPlane.getTile(i, j)
                if (tileIdx > 0) {
                    setTile(Vec2i(j, i), tileIdx)
                }
            }
        }

        val objects = actionPlane.objects
        for (wwdObject in objects) {
            val wObject = addObject()
            wObject.wwdObject = wwdObject
        }
    }

    override fun getTile(vt: Vec2i): Int {
        return tiles[vt] ?: INVISIBLE_TILE_INDEX
    }

    fun addObject(): WObject {
        val obj = WObjectImpl()
        objects.add(obj)
        onObjectAdded(obj)
        return obj
    }

    fun removeObject(obj: WObject): Boolean {
        val objImpl = obj as WObjectImpl
        objImpl.preRemoved(Unit)
        return objects.remove(obj)
    }

    private fun setTile(vt: Vec2i, tileIdx: Int) {
        tiles[vt] = tileIdx
        tilesBounds = tilesBounds.union(vt)
        tilesChanged(vt)
    }
}
