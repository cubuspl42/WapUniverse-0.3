package io.github.wapuniverse.lsd.level2

import io.github.wapuniverse.core.FormulaLevelMap
import io.github.wapuniverse.core.formula
import io.github.wapuniverse.core.formulaMap
import io.github.wapuniverse.lsd.level2.Level2AlphaTile.*


private val level2ActionFormulaMap = formulaMap(listOf(
        formula(LADDER_T, 16),
        formula(LADDER_M, 27),
        formula(LADDER_B, 22)
))

val level2FormulaMap = FormulaLevelMap(mapOf(
        "ACTION" to level2ActionFormulaMap
))
