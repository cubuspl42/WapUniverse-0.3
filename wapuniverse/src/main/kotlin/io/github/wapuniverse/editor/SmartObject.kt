package io.github.wapuniverse.editor

import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.utils.Matrix
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import java.util.*


private fun <T> mutableNCopies(n: Int, e: T): MutableList<T> {
    return ArrayList(Collections.nCopies(n, e))
}

fun <T> tiles(m: Int, n: Int, def: T): MutableList<MutableList<T>> {
    val rows: MutableList<MutableList<T>> = mutableNCopies(m, mutableListOf())
    for (i in 0..rows.size - 1) {
        rows[i] = mutableNCopies(n, def)
    }
    return rows
}

class SmartObject(tileLayer: TileLayer, width: Int, height: Int) {
    private val matrix = AlphaTileMatrix()

    init {
        resize(width, height)
        tileLayer.addMatrix(matrix)
    }

    var offset: Vec2i
        get() = matrix.offset
        set(value) {
            matrix.offset = value
        }

    val rect = matrix.rect

    fun resize(w: Int, h: Int) {
        val t = tiles(h, w, EMPTY)
        for (i in 1..h) {
            for (j in 1..w) {
                t[i - 1][j - 1] = when {
                    i == 1 && j == 1 -> BLOCK_TL
                    i == 1 && j == w - 1 -> BLOCK_TR_1
                    i == 1 && j == w -> BLOCK_TR_2
                    i == h && j == 1 -> BLOCK_BL
                    i == 1 -> BLOCK_T
                    j == w -> BLOCK_R
                    i == h -> BLOCK_M
                    j == 1 -> BLOCK_L
                    else -> BLOCK_M
                }
            }
        }
        matrix.alphaTiles = Matrix(t)
    }
}

class SmartObjectComponent {
    private val objects = HashSet<SmartObject>()

    fun addSmartObject(obj: SmartObject) {
        objects.add(obj)
        objectAdded._emit(obj)
    }

    val objectAdded = Signal<SmartObject>()
}
