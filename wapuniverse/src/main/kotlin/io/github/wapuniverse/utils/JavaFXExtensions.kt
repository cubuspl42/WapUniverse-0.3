package io.github.wapuniverse.utils

import com.sun.javafx.geom.Vec2d


infix operator fun Vec2d.minus(v: Vec2d): Vec2d {
    return Vec2d(x - v.x, y - v.y)
}
