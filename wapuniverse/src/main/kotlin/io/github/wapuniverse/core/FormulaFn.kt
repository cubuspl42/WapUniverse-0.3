package io.github.wapuniverse.core

fun applyFormulas(formulaMap: FormulaMap, set: Set<AlphaTile>): Int? {
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