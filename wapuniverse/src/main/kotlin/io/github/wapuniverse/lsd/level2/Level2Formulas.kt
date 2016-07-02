package io.github.wapuniverse.lsd.level2

import io.github.wapuniverse.core.formula
import io.github.wapuniverse.core.formulaGroup
import io.github.wapuniverse.lsd.level2.Level2AlphaTile.*


val level2FormulaGroup = formulaGroup("ACTION", listOf(
        formula(LADDER_T, 16),
        formula(LADDER_M, 27),
        formula(LADDER_B, 22)
))
