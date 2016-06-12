package io.github.wapuniverse.lsd.level1

import io.github.wapuniverse.editor.smartScript
import io.github.wapuniverse.editor.tiles
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.utils.Matrix


val blockScript = smartScript(4, 4, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (i in 1..h) {
        for (j in 1..w) {
            t[i - 1][j - 1] = when {
                i == 1 && j == 1 -> BLOCK_TL
                i == 1 && j == w - 1 -> BLOCK_TR_1
                i == 1 && j == w -> BLOCK_TR_2
                i == h && j == 1 -> BLOCK_BL
                i == 1 -> BLOCK_T
                j == w -> BLOCK_R
                i == h -> BLOCK_M
                j == 1 -> BLOCK_L
                else -> BLOCK_M
            }
        }
    }
    Matrix(t)
})

val ladderScript = smartScript(1, 3, { w, h ->
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

val spikesScript = smartScript(4, 2, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (j in 1..w) {
        for (i in 1..h) {
            t[i - 1][j - 1] = when {
                i == 1 -> SPIKE_T
                i == 2 -> SPIKE_B
                else -> EMPTY
            }
        }
    }
    Matrix(t)
})

val columnScript = smartScript(1, 4, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (i in 1..h) {
        t[i - 1][0] = when (i) {
            1 -> COLUMN_T
            h -> COLUMN_B
            else -> COLUMN_M
        }
    }
    Matrix(t)
})

val platformScript = smartScript(4, 1, { w, h ->
    val t = tiles(h, w, EMPTY)
    for (j in 1..w) {
        t[0][j - 1] = when (j) {
            1 -> PLATFORM_L
            w -> PLATFORM_R
            else -> PLATFORM_T
        }
    }
    Matrix(t)
})