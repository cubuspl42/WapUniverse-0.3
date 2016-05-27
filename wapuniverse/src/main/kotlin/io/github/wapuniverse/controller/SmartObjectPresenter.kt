package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.SmartObject
import io.github.wapuniverse.editor.SmartObjectComponent
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.view.RubberBand
import io.github.wapuniverse.view.SceneView
import javafx.geometry.Rectangle2D
import java.util.*


fun scaleDown(r: Rectangle2D): Rectangle2Di {
    val minX = Math.round(r.minX / tileWidth).toInt()
    val minY = Math.round(r.minY / tileWidth).toInt()
    val maxX = Math.round(r.maxX / tileWidth).toInt()
    val maxY = Math.round(r.maxY / tileWidth).toInt()
    return Rectangle2Di(minX, minY, maxX - minX, maxY - minY)
}

fun scaleUp(r: Rectangle2Di): Rectangle2D {
    val minX = r.minX * tileWidth
    val minY = r.minY * tileWidth
    val maxX = r.maxX * tileWidth
    val maxY = r.maxY * tileWidth
    return Rectangle2D(minX, minY, maxX - minX, maxY - minY)
}

class SmartObjectPresenter(smartObjectComponent: SmartObjectComponent, sceneView: SceneView) {
    private data class Presentation(
            val rubberBand: RubberBand
    )

    private val presentationMap = HashMap<SmartObject, Presentation>()

    init {
        smartObjectComponent.objectAdded.connect { obj ->
            val x = obj.rect.minX * tileWidth
            val y = obj.rect.minY * tileWidth
            val w = obj.rect.width * tileWidth
            val h = obj.rect.height * tileWidth
            val rubberBand = RubberBand(Vec2d(x, y), w, h)

            rubberBand.dragged.connect { r ->
                val sdr = scaleDown(r)
                obj.offset = Vec2i(sdr.minX, sdr.minY)

                val sur = scaleUp(sdr)
                rubberBand.offset = Vec2d(sur.minX, sur.minY)
            }

            rubberBand.resized.connect { r ->
                val sdr = scaleDown(r)
                if (sdr.width > 0 && sdr.height > 0) {
                    obj.resize(sdr.width, sdr.height)

                    val sur = scaleUp(sdr)
                    rubberBand.offset = Vec2d(sur.minX, sur.minY)
                    rubberBand.width = sur.width
                    rubberBand.height = sur.height
                }
            }

            sceneView.addItem(rubberBand)
            presentationMap[obj] = Presentation(rubberBand)
        }
    }
}
