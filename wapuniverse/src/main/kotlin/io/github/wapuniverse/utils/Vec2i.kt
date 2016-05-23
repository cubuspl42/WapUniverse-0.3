package io.github.wapuniverse.utils


data class Vec2i(val x: Int = 0, val y: Int = 0) {
    infix operator fun plus(v: Vec2i): Vec2i {
        return Vec2i(x + v.x, y + v.y)
    }
}
