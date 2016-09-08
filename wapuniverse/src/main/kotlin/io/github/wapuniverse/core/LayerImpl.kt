package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import java.util.*

class LayerImpl(
        private val alphaTileMapper: AlphaTileMapper,
        override val imageSet: String) : MutableLayer {

    private val INVISIBLE_TILE_ID = -1

    private val _entities = hashSetOf<Entity>()

    override val entities = _entities

    override val onEntityAdded = Signal<Entity>()

    override fun addEntity(entity: Entity) {
        _entities.add(entity)
        onEntityAdded._emit(entity)
    }

    override val onEntityRemoved = Signal<Entity>()

    override fun removeEntity(entity: Entity) {
        entity.dispose()
        _entities.remove(entity)
        onEntityRemoved._emit(entity)
    }

    private val matrices = HashSet<AlphaTileMatrix>()
    private val tileCache = HashMap<Vec2i, Int>()

    fun addMatrix(matrix: AlphaTileMatrix) {
        assert(matrix.layer == null)
        matrix.layer = this
        matrices.add(matrix)
        matrixAdded._emit(matrix)
        updateCache()
    }

    val matrixAdded = Signal<AlphaTileMatrix>()

    fun removeMatrix(matrix: AlphaTileMatrix) {
        assert(matrix.layer == this)
        matrix.layer = null
        matrices.remove(matrix)
        matrixRemoved._emit(matrix)
        updateCache()
    }

    val matrixRemoved = Signal<AlphaTileMatrix>()

    override fun getTile(i: Int, j: Int): Int {
        return tileCache[Vec2i(j, i)] ?: INVISIBLE_TILE_ID
    }

    override fun calculateBounds(): Rectangle2Di {
        val tilePositions = tileCache.map { it.key }
        val minX = tilePositions.map { it.x }.min() ?: 0
        val minY = tilePositions.map { it.y }.min() ?: 0
        val maxX = tilePositions.map { it.x }.max() ?: 0
        val maxY = tilePositions.map { it.y }.max() ?: 0
        return Rectangle2Di(minX, minY, maxX - minX + 1, maxY - minY + 1)
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

    fun updateCache() {
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
