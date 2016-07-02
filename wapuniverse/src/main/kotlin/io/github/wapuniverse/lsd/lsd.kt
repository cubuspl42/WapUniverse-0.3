package io.github.wapuniverse.lsd

import io.github.wapuniverse.core.FormulaGroup
import io.github.wapuniverse.core.AdaptiveEntity
import io.github.wapuniverse.core.SmartScript
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.lsd.level1.level1ScriptMap
import io.github.wapuniverse.lsd.level2.level2FormulaGroup
import io.github.wapuniverse.lsd.level2.level2ScriptMap

val scriptMetaMap = mapOf<Int, Map<String, SmartScript>>(
        1 to level1ScriptMap,
        2 to level2ScriptMap
)

val formulaGroupMap = mapOf<Int, FormulaGroup>(
        1 to level1FormulaGroup,
        2 to level2FormulaGroup
)
