package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.BBlock
import io.github.wapuniverse.util.Vec2i
import io.github.wapuniverse.util.div
import io.github.wapuniverse.util.times
import io.github.wapuniverse.util.toVec2i
import javafx.animation.AnimationTimer
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import java.net.URL
import java.util.*
import java.util.logging.Logger

private val T = 64.0

class MainWindowController : Initializable {
    private val log = Logger.getLogger(this.javaClass.name)

    @FXML
    private lateinit var borderPane: BorderPane

    private lateinit var canvas: ResizableCanvas

    private val bblock = BBlock(Vec2i(2, 2), mutableSetOf(Vec2i(0, 0)))

    private var brushPositionT = Vec2i()

    init {
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        canvas = makeResizableCanvas()
        borderPane.children.add(canvas)
        canvas.widthProperty().bind(borderPane.widthProperty())
        canvas.heightProperty().bind(borderPane.heightProperty())
        canvas.setOnMouseMoved { ev -> onMouseMoved(ev) }
        canvas.setOnMouseDragged { ev -> onMouseDragged(ev) }

        (object : AnimationTimer() {
            override fun handle(t: Long) {
                _draw()
            }
        }).start()
    }

    private fun makeResizableCanvas() = object : ResizableCanvas() {
        override fun draw() {
            _draw()
        }
    }

    private fun _draw() {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        gc.fill = Color.BLACK
        bblock.blocks.forEach { bpT ->
            val bp = bpT * T
            gc.fillRect(bp.x.toDouble(), bp.y.toDouble(), T, T)
        }

        gc.fill = Color.BLUE
        val brp = brushPositionT.toVec2d() * T
        gc.fillRect(brp.x, brp.y, T, T)
    }

    @FXML
    private fun handleNewLevel(ev: ActionEvent) {

    }

    private fun onMouseMoved(ev: MouseEvent) {
        brushPositionT = (Vec2d(ev.x, ev.y) / T).toVec2i()
    }

    private fun onMouseDragged(ev: MouseEvent) {
        onMouseMoved(ev)
        val mousePositionT = (Vec2d(ev.x, ev.y) / T).toVec2i()
        bblock.blocks.add(mousePositionT)
    }
}
