package io.github.wapuniverse.common.util

import javafx.geometry.Rectangle2D


class Rect2d(val minX: Double = 0.0, val minY: Double = 0.0, val width: Double = 0.0, val height: Double = 0.0) {
    companion object {
        fun fromBounds(minX: Double, minY: Double, maxX: Double, maxY: Double): Rect2d {
            assert(maxX >= minX)
            assert(maxY >= minY)
            return Rect2d(minX, minY, maxX - minX, maxY - minY)
        }

        fun fromBounds(minV: Vec2d, maxV: Vec2d): Rect2d {
            return fromBounds(minV.x, minV.y, maxV.x, maxV.y)
        }
    }

    val maxX: Double
        get() = minX + width

    val maxY: Double
        get() = minY + height

    val minV: Vec2d
        get() = Vec2d(minX, minY)

    val maxV: Vec2d
        get() = Vec2d(maxX, maxY)

    fun contains(x: Double, y: Double): Boolean {
        return x >= minX && y >= minY && x < maxX && y < maxY
    }

    fun contains(v: Vec2d) = contains(v.x, v.y)

    fun intersects(rect: Rect2d): Boolean {
        return toRectangle2D().intersects(rect.toRectangle2D())
    }

    fun toRectangle2D(): Rectangle2D {
        return Rectangle2D(minX, minY, width, height)
    }

    fun union(v: Vec2d): Rect2d {
        val minX = Math.min(v.x, minX)
        val maxX = Math.max(v.x, maxX)
        val minY = Math.min(v.y, minY)
        val maxY = Math.max(v.y, maxY)
        return fromBounds(minX, minY, maxX, maxY)
    }
}
