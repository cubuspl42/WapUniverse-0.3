package io.github.wapuniverse.core

/**
 * levelId -> formula level map
 */
data class FormulaMetaMap(
        private val map: Map<Int, FormulaLevelMap>) : Map<Int, FormulaLevelMap> by map
