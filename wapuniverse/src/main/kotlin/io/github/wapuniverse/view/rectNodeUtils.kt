package io.github.wapuniverse.view

import io.github.wapuniverse.utils.Rectangle2Di
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.transform.Affine

fun drawRect(gc: GraphicsContext, transform: Affine, rect: Rectangle2Di, fill: Color?, stroke: Color) {
    val v0 = transform.transform(rect.minX.toDouble(), rect.minY.toDouble())
    val v1 = transform.transform(rect.maxX.toDouble(), rect.maxY.toDouble())
    val w = v1.x - v0.x
    val h = v1.y - v0.y
    gc.fill = fill
    gc.stroke = stroke
    if(fill != null) {
        gc.globalAlpha = 0.5
        gc.fillRect(v0.x, v0.y, w, h)
        gc.globalAlpha = 1.0
    }
    gc.strokeRect(v0.x, v0.y, w, h)
}
