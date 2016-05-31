package io.github.wapuniverse.editor

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.utils.*
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

interface SmartScript {
    val defaultWidth: Int
    val defaultHeight: Int
    fun run(width: Int, height: Int): Matrix<AlphaTile>
}

fun smartScript(defaultWidth: Int, defaultHeight: Int, f: (w: Int, h: Int) -> Matrix<AlphaTile>): SmartScript {
    return object : SmartScript {
        override val defaultWidth = defaultWidth
        override val defaultHeight = defaultHeight
        override fun run(width: Int, height: Int): Matrix<AlphaTile> {
            return f(width, height)
        }
    }
}

val blockScript = smartScript(4, 4, { w, h ->
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
    Matrix(t)
})

val ladderScript = smartScript(1, 3, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (i in 1..h) {
        t[i - 1][0] = when (i) {
            1 -> LADDER_T
            h -> LADDER_B
            else -> LADDER_M
        }
    }
    Matrix(t)
})

val spikesScript = smartScript(4, 2, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (j in 1..w) {
        for (i in 1..h) {
            t[i - 1][j - 1] = when {
                i == 1 -> SPIKE_T
                i == 2 -> SPIKE_B
                else -> EMPTY
            }
        }
    }
    Matrix(t)
})

val columnScript = smartScript(1, 4, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (i in 1..h) {
        t[i - 1][0] = when (i) {
            1 -> COLUMN_T
            h -> COLUMN_B
            else -> COLUMN_M
        }
    }
    Matrix(t)
})

val platformScript = smartScript(4, 1, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (j in 1..w) {
        t[0][j - 1] = when (j) {
            1 -> PLATFORM_L
            w -> PLATFORM_R
            else -> PLATFORM_T
        }
    }
    Matrix(t)
})

class SmartObject(tileLayer: TileLayer, private val script: SmartScript) {
    private val matrix = AlphaTileMatrix()

    val changed = Signal<SmartObject>()

    init {
        resize(script.defaultWidth, script.defaultHeight)
        tileLayer.addMatrix(matrix)
    }

    var offset: Vec2i
        get() = matrix.offset
        set(value) {
            matrix.offset = value
            changed._emit(this)
        }

    val rect: Rectangle2Di
        get() = matrix.rect

    fun resize(w: Int, h: Int) {
        matrix.alphaTiles = script.run(w, h)
        changed._emit(this)
    }

    var position: Vec2d
        get() = Vec2d(offset.x * tileWidth, offset.y * tileWidth)
        set(value) {
            offset = (value / tileWidth).toVec2i()
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
