package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.Signal
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.GraphicsContext

class RubberBandWidget(
        var rect: Rectangle2D
) : UiWidget {
    private var _desiredRect = rect

    var desiredRect: Rectangle2D
        set(value) {
            onDesiredRectChanged._emit(this)
            _desiredRect = value
        }
        get() = _desiredRect

    val onDesiredRectChanged = Signal<RubberBandWidget>()

    fun describeArea(lp: Vec2d) {

    }

    fun draw(gc: GraphicsContext) {

    }
}
