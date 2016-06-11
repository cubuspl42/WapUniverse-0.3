package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import javafx.geometry.Rectangle2D


class SBoxComponent {
    private val _sBoxes = hashSetOf<SBox>()

    val sBoxes: Set<SBox> = _sBoxes

    fun addSBox(sBox: SBox) {
        _sBoxes.add(sBox)
    }

    fun removeSBox(sBox: SBox) {
        _sBoxes.remove(sBox)
    }

    fun query(rect: Rectangle2D): List<SBox> {
        return sBoxes.filter { it.boundingRect.intersects(rect) }
    }

    fun query(p: Vec2d): List<SBox> {
        return query(Rectangle2D(p.x, p.y, p.x, p.y))
    }

    fun hover(p: Vec2d) {
        sBoxes.forEach { it.isHovered = false }
        query(p).forEach { it.isHovered = true }
    }

    fun select(rect: Rectangle2D) {
        query(rect).forEach { it.isSelected = true }
    }

    fun selectAll() {
        sBoxes.forEach { it.isSelected = true }
    }

    fun unselectAll() {
        sBoxes.forEach { it.isSelected = false }
    }
}
