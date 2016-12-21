package io.github.wapuniverse

import io.github.wapuniverse.util.Vec2i
import javafx.scene.paint.Color

private val brushColor = Color.BLACK

enum class BrushState {
    PRESSED,
    RELEASED
}

class Brush(private val pxCanvas: PxCanvas) {
    var state = BrushState.RELEASED
        set(value) {
            if (value == BrushState.PRESSED) pxCanvas.setPixel(position, brushColor)
            field = value
        }

    var position = Vec2i()
        set(value) {
            if (state == BrushState.PRESSED) {
                pxCanvas.setPixel(value, brushColor)
            }
            field = value
        }
}
