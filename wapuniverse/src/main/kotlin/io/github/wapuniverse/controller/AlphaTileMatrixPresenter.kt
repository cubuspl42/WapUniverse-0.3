package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.AlphaTileMatrix
import io.github.wapuniverse.editor.TileLayer
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.view.RubberBand
import io.github.wapuniverse.view.SceneView
import javafx.geometry.Rectangle2D
import java.util.*


private fun scaleDown(r: Rectangle2D): Rectangle2Di {
    val minX = Math.round(r.minX / tileWidth).toInt()
    val minY = Math.round(r.minY / tileWidth).toInt()
    val maxX = Math.round(r.maxX / tileWidth).toInt()
    val maxY = Math.round(r.maxY / tileWidth).toInt()
    return Rectangle2Di(minX, minY, maxX - minX, maxY - minY)
}

private fun scaleUp(r: Rectangle2Di): Rectangle2D {
    val minX = r.minX * tileWidth
    val minY = r.minY * tileWidth
    val maxX = r.maxX * tileWidth
    val maxY = r.maxY * tileWidth
    return Rectangle2D(minX, minY, maxX - minX, maxY - minY)
}

private data class Presentation(
        val rubberBand: RubberBand
)

class AlphaTileMatrixPresenter(tileLayer: TileLayer, sceneView: SceneView) {
    private val presentationMap = HashMap<AlphaTileMatrix, Presentation>()

    init {
        tileLayer.matrixAdded.connect { mx ->
            val x = mx.rect.minX * tileWidth
            val y = mx.rect.minY * tileWidth
            val w = mx.rect.width * tileWidth
            val h = mx.rect.height * tileWidth
            val rubberBand = RubberBand(Vec2d(x, y), w, h)

            rubberBand.dragged.connect { r ->
                val sdr = scaleDown(r)
                val sur = scaleUp(sdr)
                mx.offset = Vec2i(sdr.minX, sdr.minY)
                rubberBand.offset = Vec2d(sur.minX, sur.minY)
            }

            rubberBand.resized.connect { r ->
                val sdr = scaleDown(r)
                if (sdr.width > 0 && sdr.height > 0) {
                    val sur = scaleUp(sdr)
                    rubberBand.offset = Vec2d(sur.minX, sur.minY)
                    rubberBand.width = sur.width
                    rubberBand.height = sur.height
                }
            }

            sceneView.addItem(rubberBand)
            presentationMap[mx] = Presentation(rubberBand)
        }
    }
}
