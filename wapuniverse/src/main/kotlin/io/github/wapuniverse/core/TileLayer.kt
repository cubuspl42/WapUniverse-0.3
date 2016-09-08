package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Matrix
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import java.util.*


val tileWidth = 64.0

class AlphaTileMatrix {
    internal var layer: LayerImpl? = null

    var offset = Vec2i()
        set(value) {
            if (field != value) {
                field = value
                layer?.updateCache()
            }
        }

    var alphaTiles = Matrix<AlphaTile>()
        set(value) {
            field = value
            layer?.updateCache()
        }

    val rect: Rectangle2Di
        get() = Rectangle2Di(offset.x, offset.y, alphaTiles.width, alphaTiles.height)


    fun getAlphaTile(ai: Int, aj: Int): AlphaTile {
        val ri = ai - offset.y
        val rj = aj - offset.x
        return alphaTiles.getElement(ri, rj)
    }
}
