package io.github.wapuniverse.utils

import javafx.geometry.Rectangle2D


class Rectangle2Di(val minX: Int, val minY: Int, val width: Int, val height: Int) {
    val maxX: Int
        get() = minX + width

    val maxY: Int
        get() = minY + height

    fun contains(x: Int, y: Int): Boolean {
        return x >= minX && y >= minY && x < maxX && y < maxY
    }

    fun toRectangle2D(): Rectangle2D {
        return Rectangle2D(minX.toDouble(), minY.toDouble(), width.toDouble(), height.toDouble())
    }
}
