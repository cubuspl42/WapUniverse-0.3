package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Matrix

fun smartScript(
        name: String, defaultWidth: Int, defaultHeight: Int, f: (w: Int, h: Int) -> Matrix<AlphaTile>): SmartScript {
    return object : SmartScript {
        override val name = name
        override val defaultWidth = defaultWidth
        override val defaultHeight = defaultHeight
        override fun run(width: Int, height: Int): Matrix<AlphaTile> {
            return f(width, height)
        }
    }
}

fun makeSmartScriptMap(vararg scripts: SmartScript): Map<String, SmartScript> {
    return scripts.map { Pair(it.name, it) }.toMap()
}
