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
    },
    LADDER_T,
    LADDER_M,
    LADDER_B,
    SPIKE_T {
        override val z = 1200
    },
    SPIKE_B {
        override val z = 1200
    };

    override val z = 0
}
