package io.github.wapuniverse.lsd

import io.github.wapuniverse.core.FormulaMetaMap
import io.github.wapuniverse.core.ScriptMetaMap
import io.github.wapuniverse.lsd.level1.level1FormulaMap
import io.github.wapuniverse.lsd.level1.level1ScriptMap
import io.github.wapuniverse.lsd.level2.level2FormulaMap
import io.github.wapuniverse.lsd.level2.level2ScriptMap

val scriptMetaMap = ScriptMetaMap(mapOf(
        1 to level1ScriptMap,
        2 to level2ScriptMap
))

val formulaMetaMap = FormulaMetaMap(mapOf(
        1 to level1FormulaMap,
        2 to level2FormulaMap
))
