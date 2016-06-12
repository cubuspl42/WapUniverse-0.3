package io.github.wapuniverse.editor

import io.github.wapuniverse.utils.Matrix

fun smartScript(defaultWidth: Int, defaultHeight: Int, f: (w: Int, h: Int) -> Matrix<AlphaTile>): SmartScript {
    return object : SmartScript {
        override val defaultWidth = defaultWidth
        override val defaultHeight = defaultHeight
        override fun run(width: Int, height: Int): Matrix<AlphaTile> {
            return f(width, height)
        }
    }
}