package io.github.wapuniverse.core

/**
 * set of alphatiles -> wap32 tile
 */
data class FormulaMap(private val map: Map<Set<Int>, Int>) : Map<Set<Int>, Int> by map
