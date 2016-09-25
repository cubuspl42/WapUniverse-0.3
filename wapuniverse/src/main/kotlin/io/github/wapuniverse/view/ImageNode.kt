package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.ImageSetDatabase
import io.github.wapuniverse.core.Layer
import io.github.wapuniverse.core.World
import io.github.wapuniverse.core.tileWidth
import io.github.wapuniverse.utils.div
import io.github.wapuniverse.utils.toVec2i
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import java.util.*

class ImageNode(
        private val image: Image,
        private val x: Double,
        private val y: Double
) {
    val z = 0

    fun draw(gc: GraphicsContext) = gc.drawImage(image, x, y)
}
