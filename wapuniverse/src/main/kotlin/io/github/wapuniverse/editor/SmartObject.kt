package io.github.wapuniverse.editor

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

val smartObjectLogicName = "_WU_SmartObject"

class SmartObject(private val tileLayer: TileLayer, private val script: SmartScript) : Entity() {
    class Loader(private val tileLayer: TileLayer) : EntityLoader {
        override val logicName = smartObjectLogicName

        override fun load(levelIndex: Int, wwdObject: WwdObject): SmartObject {
            val scriptMap = scriptMetaMap[levelIndex]!!
            val script = scriptMap[wwdObject.name]!!
            val entity = SmartObject(tileLayer, script)
            entity.position = Vec2i(wwdObject.x, wwdObject.y)
            entity.resize(wwdObject.width, wwdObject.height)
            return entity
        }
    }

    override fun dump(): WwdObject {
        val wwdObject = WwdObject()
        with (wwdObject) {
            logic = smartObjectLogicName
            name = script.name
            x = position.x + tileWidth.toInt() / 2 - 1
            y = position.y + tileWidth.toInt() / 2 - 1
            width = rect.width
            height = rect.height
        }
        return wwdObject
    }

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
        tileLayer.removeMatrix(matrix)
    }
}
