package io.github.wapuniverse.core

import io.github.wapuniverse.wap32.Wwd

class World(
        val levelIndex: Int,
        val wwd: Wwd,
        formulaLevelMap: FormulaLevelMap,
        scriptMap: ScriptMap,
        imageSet: String
) {
    val primaryLayer: MutableLayer = LayerImpl(formulaLevelMap, scriptMap, imageSet)
}