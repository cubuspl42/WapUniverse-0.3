package io.github.wapuniverse

import io.github.wapuniverse.util.Vec2i
import javafx.scene.paint.Color

class PxCanvas {
    private val _pixels = mutableMapOf<Vec2i, Color>()

    fun setPixel(p: Vec2i, c: Color) {
        _pixels[p] = c
    }

    fun getPixel(p: Vec2i): Color {
        return _pixels.getOrElse(p, { Color.TRANSPARENT })
    }

    val pixels: Map<Vec2i, Color> = _pixels
}
