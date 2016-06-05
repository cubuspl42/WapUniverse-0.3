package io.github.wapuniverse.lsd.level1

import io.github.wapuniverse.editor.AlphaTile


enum class Level1AlphaTile : AlphaTile {
    EMPTY {
        override val z = -100
    },
    BLOCK_TL {
        override val z = 800
    },
    BLOCK_T {
        override val z = 900
    },
    BLOCK_TR_1 {
        override val z = 800
    },
    BLOCK_TR_2 {
        override val z = 800
    },
    BLOCK_R {
        override val z = 900
    },
    BLOCK_BL {
        override val z = 800
    },
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
    },
    COLUMN_T {
        override val z = 1500
    },
    COLUMN_M {
        override val z = 1500
    },
    COLUMN_B {
        override val z = 1500
    },
    PLATFORM_L,
    PLATFORM_T,
    PLATFORM_R;

    override val z = 0
}
