package io.github.wapuniverse.editor

import io.github.wapuniverse.editor.AlphaTile


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

fun formulaGroup(imageSet: String, formulas: List<Formula>): FormulaGroup {
    return FormulaGroup(imageSet, formulas)
}
