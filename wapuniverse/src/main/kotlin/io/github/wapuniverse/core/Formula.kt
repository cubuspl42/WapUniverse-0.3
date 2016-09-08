package io.github.wapuniverse.core

import io.github.wapuniverse.core.AlphaTile


data class Formula(
        val alphaTileSet: Set<Int>,
        val tileIndex: Int
)

fun formula(alphaTileSet: Set<AlphaTile>, tileIndex: Int): Formula {
    val ordinalSet = alphaTileSet.map { it.ordinal }.toSet()
    return Formula(ordinalSet, tileIndex)
}

fun formula(alphaTile: AlphaTile, tileIndex: Int): Formula {
    return formula(setOf(alphaTile), tileIndex)
}

data class FormulaGroup(
        val imageSet: String,
        val formulas: List<Formula>
)

fun formulaMap(formulas: List<Formula>): FormulaMap {
    return FormulaMap(formulas.map { Pair(it.alphaTileSet, it.tileIndex) }.toMap())
}
