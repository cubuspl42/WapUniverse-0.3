package io.github.wapuniverse.editor

import io.github.wapuniverse.utils.Matrix
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import java.util.*


val tileWidth = 64.0

class AlphaTileMatrix {
    internal var _tileLayer: TileLayer? = null

    var offset = Vec2i()
        set(value) {
            if (field != value) {
                field = value
                _tileLayer?._updateCache()
            }
        }

    var alphaTiles = Matrix<AlphaTile>()
        set(value) {
            field = value
            _tileLayer?._updateCache()
        }

    val rect: Rectangle2Di
        get() = Rectangle2Di(offset.x, offset.y, alphaTiles.width, alphaTiles.height)


    fun getAlphaTile(ai: Int, aj: Int): AlphaTile {
        val ri = ai - offset.y
        val rj = aj - offset.x
        return alphaTiles.getElement(ri, rj)
    }
}

private val DEFAULT_TILE_INDEX = -2

class TileLayer(
        private val alphaTileMapper: AlphaTileMapper,
        val imageSet: String
) {
    private val matrices = HashSet<AlphaTileMatrix>()
    private val tileCache = HashMap<Vec2i, Int>()

    fun addMatrix(matrix: AlphaTileMatrix) {
        assert(matrix._tileLayer == null)
        matrix._tileLayer = this
        matrices.add(matrix)
        matrixAdded._emit(matrix)
        _updateCache()
    }

    val matrixAdded = Signal<AlphaTileMatrix>()

    fun getTile(i: Int, j: Int): Int {
        return tileCache[Vec2i(j, i)] ?: DEFAULT_TILE_INDEX
    }

    private fun cacheTile(p: Vec2i) {
        val alphaTileSet = matrices
                .filter { it.rect.contains(p.x, p.y) }
                .map { it.getAlphaTile(p.y, p.x) }
                .toSet()
        val tileIndex = alphaTileMapper.mapAlphaTileSet(alphaTileSet)
        if (tileIndex != null) {
            tileCache[p] = tileIndex
        }
    }

    internal fun _updateCache() {
        tileCache.clear()
        matrices
                .forEach {
                    val r = it.rect
                    for (i in 0..r.height - 1) {
                        for (j in 0..r.width - 1) {
                            val p = it.offset + Vec2i(j, i)
                            cacheTile(p)
                        }
                    }
                }
    }
}
