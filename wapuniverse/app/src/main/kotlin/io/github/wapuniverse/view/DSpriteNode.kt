package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.Vec2d
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image


class DSpriteNode(val image: Image, val anchor: Vec2d) : DNode() {
    var position = Vec2d()

    var scale = Vec2d(1.0, 1.0)

    var alpha = 1.0

    override fun draw(gc: GraphicsContext) {
        gc.save()
        gc.translate(position.x, position.y)
        gc.scale(scale.x, scale.y)
        gc.translate(-anchor.x, -anchor.y)
        gc.globalAlpha = alpha
        gc.drawImage(image, 0.0, 0.0)
        gc.restore()
    }
}
