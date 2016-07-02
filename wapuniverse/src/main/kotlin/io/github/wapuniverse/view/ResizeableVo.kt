package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.controller.SceneInputController
import io.github.wapuniverse.core.AdaptiveEntity
import io.github.wapuniverse.core.tileWidth
import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Vec2i
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.paint.Color


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

class ResizeableVo(
        private val sceneView: SceneView,
        private val sceneInputController: SceneInputController,
        private val adaptiveEntity: AdaptiveEntity,
        private val sBoxComponent: SBoxComponent,
        private val root: Node
) {
    private val sBox = SBox(adaptiveEntity, scaleUp(adaptiveEntity.rect))

    private val rubberBand = RubberBand(scaleUp(adaptiveEntity.rect), root)

    init {
        sBoxComponent.addSBox(sBox)
        sceneView.addItem(rubberBand)
        updateColor()

        adaptiveEntity.disposed.on {
            sBoxComponent.removeSBox(sBox)
            sceneView.removeItem(rubberBand)
            sceneInputController.removeInputHandler(rubberBand)
        }
    }

    private fun updateColor() {
        rubberBand.color = when {
            sBox.isSelected -> Color.RED
            sBox.isHovered -> Color.BLUE
            else -> Color.LIGHTBLUE
        }
    }

    private val slotSoc = adaptiveEntity.changed.on {
        val sur = scaleUp(adaptiveEntity.rect)
        sBox.boundingRect = sur
        rubberBand.offset = Vec2d(sur.minX, sur.minY)
        rubberBand.width = sur.width
        rubberBand.height = sur.height
    }

    private val slotSh = sBox.hovered.on {
        updateColor()
    }

    private val slotSuh = sBox.unhovered.on {
        updateColor()
    }

    private val slotSs = sBox.selected.on {
        rubberBand.z = 100000.0
        updateColor()
        sceneInputController.addInputHandler(rubberBand)
    }

    private val slotSus = sBox.unselected.on {
        rubberBand.z = 0.0
        updateColor()
        sceneInputController.removeInputHandler(rubberBand)
    }

    private val slotRr = rubberBand.resized.on { r ->
        val sdr = scaleDown(r)
        if (sdr.width > 0 && sdr.height > 0) {
            adaptiveEntity.offset = Vec2i(sdr.minX, sdr.minY)
            adaptiveEntity.resize(sdr.width, sdr.height)
        }
    }
}