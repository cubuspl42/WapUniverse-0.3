package io.github.wapuniverse.lsd.level1

import io.github.wapuniverse.editor.AlphaTile


enum class Level1AlphaTile : AlphaTile {
    EMPTY,
    BLOCK_TL,
    BLOCK_T {
        override val z = 900
    },
    BLOCK_TR_1,
    BLOCK_TR_2,
    BLOCK_R {
        override val z = 900
    },
    BLOCK_BL,
    BLOCK_L {
        override val z = 900
    },
    BLOCK_M {
        override val z = 1000
    };

    override val z = 0
}
