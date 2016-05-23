package io.github.wapuniverse.utils


class Rectangle2Di(private val minX: Int, private val minY: Int, private val width: Int, private val  height: Int) {
    val maxX: Int
        get() = minX + width

    val maxY: Int
        get() = minY + height

    fun contains(x: Int, y: Int): Boolean {
        return x >= minX && y >= minY && x < maxX && y < maxY
    }
}
