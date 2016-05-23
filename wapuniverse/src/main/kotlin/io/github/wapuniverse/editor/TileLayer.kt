package io.github.wapuniverse.editor

import io.github.wapuniverse.utils.Matrix


private val DEFAULT_TILE_INDEX = -2

class TileLayer(
        private val tileMatrix: Matrix<Int>,
        val imageSet: String
) {
    fun getTile(i: Int, j: Int): Int {
        if (i >= 0 && i < tileMatrix.height && j >= 0 && j < tileMatrix.height) {
            return tileMatrix.getElement(i, j)
        } else {
            return DEFAULT_TILE_INDEX
        }
    }
}
