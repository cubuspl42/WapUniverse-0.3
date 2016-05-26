package io.github.wapuniverse.utils

import com.sun.javafx.geom.Vec2d
import javafx.geometry.Point2D


infix operator fun Vec2d.minus(v: Vec2d): Vec2d {
    return Vec2d(x - v.x, y - v.y)
}


infix operator fun Point2D.minus(v: Vec2d): Point2D {
    return Point2D(x - v.x, y - v.y)
}
