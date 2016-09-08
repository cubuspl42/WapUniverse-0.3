package io.github.wapuniverse.core

/**
 * imageSet id -> formula map
 */
data class FormulaLevelMap(private val map: Map<String, FormulaMap>) : Map<String, FormulaMap> by map
