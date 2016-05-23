package io.github.wapuniverse.lsd.level1

import io.github.wapuniverse.lsd.formula
import io.github.wapuniverse.lsd.formulaGroup
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*


val level1FormulaGroup = formulaGroup("ACTION", listOf(
        formula(BLOCK_TL, 303),
        formula(BLOCK_T, 304),
        formula(BLOCK_TR_1, 305),
        formula(BLOCK_TR_2, 307),
        formula(BLOCK_R, 308),
        formula(BLOCK_BL, 920),
        formula(BLOCK_L, 302),
        formula(BLOCK_M, 12),
        formula(setOf(BLOCK_T, BLOCK_L), 74),
        formula(setOf(BLOCK_T, BLOCK_BL), 74),
        formula(setOf(BLOCK_TL, BLOCK_BL), 302),
        formula(setOf(BLOCK_T, BLOCK_R), 309),
        formula(setOf(BLOCK_TR_1, BLOCK_L), 74),
        formula(setOf(BLOCK_TR_1, BLOCK_BL), 74)
))
