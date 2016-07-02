package io.github.wapuniverse.core

import io.github.wapuniverse.core.FormulaGroup
import java.util.*


class AlphaTileMapper(formulaGroup: FormulaGroup) {
    val formulaMap = HashMap<Set<Int>, Int>()

    init {
        formulaGroup.formulas.forEach {
            formulaMap[it.alphaTileSet] = it.tileIndex
        }
    }

    fun mapAlphaTileSet(set: Set<AlphaTile>): Int? {
        val stack = set.toList()
                .sortedBy { it.z }
                .map { it.ordinal }
        for (i in 0..stack.size - 1) {
            val subSet = stack.subList(i, stack.size).toSet()
            val tileIndex = formulaMap[subSet]
            if (tileIndex != null) {
                return tileIndex
            }
        }
        return null
    }
}




