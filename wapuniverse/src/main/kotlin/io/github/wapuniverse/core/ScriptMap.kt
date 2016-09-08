package io.github.wapuniverse.core

/**
 * smart script id -> smart script
 */
data class ScriptMap(private val map: Map<String, SmartScript>) : Map<String, SmartScript> by map
