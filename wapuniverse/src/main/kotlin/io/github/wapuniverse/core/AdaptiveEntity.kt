package io.github.wapuniverse.core

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.lsd.scriptMetaMap
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.wap32.WwdObject
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
    val name: String
    val defaultWidth: Int
    val defaultHeight: Int
    fun run(width: Int, height: Int): Matrix<AlphaTile>
}

val adaptiveEntityLogicName = "_WU_SmartObject"

class AdaptiveEntity(private val layer: LayerImpl, private val script: SmartScript) : EntityImpl() {
    class Loader() : EntityLoader {
        override val logicName = adaptiveEntityLogicName

        override fun load(levelIndex: Int, wwdObject: WwdObject): EntityRepr {
            return AdaptiveEntityRepr(
                    scriptId = wwdObject.name,
                    position = Vec2i(wwdObject.x, wwdObject.y)
            )
        }
    }

    override fun dump(): WwdObject {
        val wwdObject = WwdObject()
        with (wwdObject) {
            logic = adaptiveEntityLogicName
            name = script.name
            x = position.x + tileWidth.toInt() / 2 - 1
            y = position.y + tileWidth.toInt() / 2 - 1
            width = rect.width
            height = rect.height
        }
        return wwdObject
    }

    private val matrix = AlphaTileMatrix()

    val changed = Signal<AdaptiveEntity>()

    init {
        resize(script.defaultWidth, script.defaultHeight)
        layer.addMatrix(matrix)
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
        if (rect.width != w || rect.height != h) {
            matrix.alphaTiles = script.run(w, h)
            changed._emit(this)
        }
    }

    override var position: Vec2i
        get() = Vec2d(offset.x * tileWidth, offset.y * tileWidth).toVec2i()
        set(value) {
            val v = value.toVec2d() / tileWidth
            val x = Math.round(v.x).toInt()
            val y = Math.round(v.y).toInt()
            offset = Vec2i(x, y)
        }

    override fun onDispose() {
        layer.removeMatrix(matrix)
    }
}
