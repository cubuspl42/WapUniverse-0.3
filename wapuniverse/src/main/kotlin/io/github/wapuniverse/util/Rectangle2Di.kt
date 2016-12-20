package io.github.wapuniverse.util

import javafx.geometry.Rectangle2D


class Rectangle2Di(val minX: Int = 0, val minY: Int = 0, val width: Int = 0, val height: Int = 0) {
    val maxX: Int
        get() = minX + width

    val maxY: Int
        get() = minY + height

    val minV: Vec2i
        get() = Vec2i(minX, minY)

    val maxV: Vec2i
        get() = Vec2i(maxY, maxY)

    fun contains(x: Int, y: Int): Boolean {
        return x >= minX && y >= minY && x < maxX && y < maxY
    }

    fun contains(x: Double, y: Double): Boolean {
        return x >= minX && y >= minY && x < maxX && y < maxY
    }

    fun intersects(rect: Rectangle2Di): Boolean {
        return toRectangle2D().intersects(rect.toRectangle2D())
    }

    fun toRectangle2D(): Rectangle2D {
        return Rectangle2D(minX.toDouble(), minY.toDouble(), width.toDouble(), height.toDouble())
    }
}
