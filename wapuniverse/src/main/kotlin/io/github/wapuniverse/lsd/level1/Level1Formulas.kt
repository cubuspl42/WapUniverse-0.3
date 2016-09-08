package io.github.wapuniverse.lsd.level1

import io.github.wapuniverse.core.FormulaLevelMap
import io.github.wapuniverse.core.formula
import io.github.wapuniverse.core.formulaMap
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*


private val level1ActionFormulaMap = formulaMap(listOf(
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
        formula(setOf(BLOCK_TR_1, BLOCK_BL), 74),
        formula(LADDER_T, 310),
        formula(LADDER_M, 311),
        formula(LADDER_B, 312),
        formula(SPIKE_T, 326),
        formula(SPIKE_B, 327),
        formula(setOf(BLOCK_R, SPIKE_T), 319),
        formula(setOf(BLOCK_T, BLOCK_R, SPIKE_B), 322),
        formula(setOf(SPIKE_T, BLOCK_L), 321),
        formula(setOf(BLOCK_T, BLOCK_L, SPIKE_B), 925),
        formula(setOf(BLOCK_T, BLOCK_BL, SPIKE_B), 925),
        formula(setOf(BLOCK_TR_1, BLOCK_L, SPIKE_B), 925),
        formula(COLUMN_T, 933),
        formula(COLUMN_M, 934),
        formula(COLUMN_B, 935),
        formula(PLATFORM_L, 331),
        formula(PLATFORM_T, 332),
        formula(PLATFORM_R, 334)
))

val level1FormulaMap = FormulaLevelMap(mapOf(
        "ACTION" to level1ActionFormulaMap
))
