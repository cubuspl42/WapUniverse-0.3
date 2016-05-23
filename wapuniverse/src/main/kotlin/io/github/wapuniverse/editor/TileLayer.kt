package io.github.wapuniverse.editor

import io.github.wapuniverse.utils.Matrix
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Vec2i
import java.util.*


class AlphaTileMatrix {
    var offset = Vec2i()

    var alphaTiles = Matrix<AlphaTile>()

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

    fun addMatrix(matrix: AlphaTileMatrix) {
        matrices.add(matrix)
    }

    fun getTile(i: Int, j: Int): Int {
        val alphaTileSet = matrices
            .filter { it.rect.contains(j, i) }
            .map { it.getAlphaTile(i, j) }
            .toSet()
        val tileIndex = alphaTileMapper.mapAlphaTileSet(alphaTileSet)
        if (tileIndex != null) {
            return tileIndex
        } else {
            return DEFAULT_TILE_INDEX
        }
    }
}
