package io.github.wapuniverse.lsd.level2

import io.github.wapuniverse.core.*
import io.github.wapuniverse.lsd.level2.Level2AlphaTile.*
import io.github.wapuniverse.utils.Matrix


// TODO: No C&P
val level2ScriptMap: ScriptMap = makeSmartScriptMap(
        smartScript("Ladder", 1, 3, {
            w, h ->
            val t = tiles(h, w, EMPTY)
            for (i in 1..h) {
                t[i - 1][0] = when (i) {
                    1 -> LADDER_T
                    h -> LADDER_B
                    else -> LADDER_M
                }
            }
            Matrix(t)
        })
)
