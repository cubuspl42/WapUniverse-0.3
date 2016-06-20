package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.controller.InputHandler
import io.github.wapuniverse.controller.InputHandlerImpl
import io.github.wapuniverse.editor.SmartObject
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_NOT_HANDLED
import javafx.geometry.Rectangle2D
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import java.util.logging.Logger


class SceneImage(
        var offset: Vec2d,
        private val image: Image
) : SceneItem() {

    override val boundingBox: Rectangle2D
        get() = Rectangle2D(offset.x, offset.y, image.width, image.height)

//    private fun invTr(x: Double, y: Double) = scene!!.invTransform.transform(x, y).toVec2d()

    override fun render(gc: GraphicsContext) {
        gc.drawImage(image, offset.x, offset.y)
        gc.stroke = Color.BLACK
        gc.strokeRect(offset.x, offset.y, image.width, image.height)
    }
}
