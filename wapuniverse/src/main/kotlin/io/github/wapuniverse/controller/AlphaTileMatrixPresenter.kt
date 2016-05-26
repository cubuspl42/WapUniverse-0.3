package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.AlphaTileMatrix
import io.github.wapuniverse.editor.TileLayer
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.view.RubberBand
import io.github.wapuniverse.view.SceneView
import java.util.*


private data class Presentation(
        val rubberBand: RubberBand
)

class AlphaTileMatrixPresenter(tileLayer: TileLayer, sceneView: SceneView)  {
    private val presentationMap = HashMap<AlphaTileMatrix, Presentation>()

    init {
        tileLayer.matrixAdded.connect { mx ->
            val x = mx.rect.minX * tileWidth
            val y = mx.rect.minY * tileWidth
            val w = mx.rect.width * tileWidth
            val h = mx.rect.height * tileWidth
            val rubberBand = RubberBand(Vec2d(x, y), w, h)

            rubberBand.dragged.connect { r ->
                val rbx = (r.minX / tileWidth).toInt()
                val rby = (r.minY / tileWidth).toInt()
                mx.offset = Vec2i(rbx.toInt(), rby.toInt())
                rubberBand.offset = Vec2d(rbx * tileWidth, rby * tileWidth)
            }

            sceneView.addItem(rubberBand)
            presentationMap[mx] = Presentation(rubberBand)
        }
    }
}
