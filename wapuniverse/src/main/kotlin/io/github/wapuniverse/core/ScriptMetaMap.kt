package io.github.wapuniverse.core

/**
 * levelId -> script map
 */
data class ScriptMetaMap(private val map: Map<Int, ScriptMap>) : Map<Int, ScriptMap> by map
