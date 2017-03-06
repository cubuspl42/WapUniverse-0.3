package io.github.wapuniverse.common.util

import io.github.wapuniverse.common.util.Vec2d
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D


infix operator fun Vec2d.minus(v: Vec2d): Vec2d {
    return Vec2d(x - v.x, y - v.y)
}

infix operator fun Vec2d.plus(v: Vec2d): Vec2d {
    return Vec2d(x + v.x, y + v.y)
}

infix operator fun Point2D.minus(v: Vec2d): Point2D {
    return Point2D(x - v.x, y - v.y)
}

infix operator fun Vec2d.times(scale: Double): Vec2d {
    return Vec2d(x * scale, y * scale)
}

fun Point2D.toVec2d(): Vec2d {
    return Vec2d(x, y)
}

fun Vec2d.toPoint2D(): Point2D {
    return Point2D(x, y)
}

infix operator fun Vec2d.div(tileWidth: Double): Vec2d {
    return Vec2d(x / tileWidth, y / tileWidth)
}

fun Vec2d.toVec2i(): Vec2i {
    return Vec2i(x.toInt(), y.toInt())
}

infix operator fun Vec2i.times(a: Double): Vec2i {
    return Vec2i(x * a.toInt(), y * a.toInt())
}

infix operator fun Vec2i.div(a: Double): Vec2i {
    return Vec2i(x / a.toInt(), y / a.toInt())
}

fun Rectangle2D.toRect2Di(): Rectangle2Di {
    return Rectangle2Di(minX.toInt(), minY.toInt(), width.toInt(), height.toInt())
}

fun Point2D.toVec2i(): Vec2i {
    return toVec2d().toVec2i()
}

fun Vec2d.toSize(): Size {
    return Size(x, y)
}

fun Vec2d.round(): Vec2i {
    return Vec2i(Math.round(x).toInt(), Math.round(y).toInt())
}

fun Vec2d.floor(): Vec2d {
    return Vec2d(Math.floor(x), Math.floor(y))
}

val Vec2d.width: Double
    get() = Math.abs(x)

val Vec2d.height: Double
    get() = Math.abs(y)
