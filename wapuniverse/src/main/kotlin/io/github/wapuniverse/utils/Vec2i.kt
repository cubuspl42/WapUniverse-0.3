package io.github.wapuniverse.utils

import com.sun.javafx.geom.Vec2d


data class Vec2i(val x: Int = 0, val y: Int = 0) {
    fun toVec2d(): Vec2d {
        return Vec2d(x.toDouble(), y.toDouble())
    }

    infix operator fun minus(v: Vec2i): Vec2i {
        return Vec2i(x - v.x, y - v.y)
    }

    infix operator fun plus(v: Vec2i): Vec2i {
        return Vec2i(x + v.x, y + v.y)
    }
}
