package io.github.wapuniverse

import io.github.wapuniverse.util.Vec2i

data class BBlock(
    var position: Vec2i = Vec2i(),
    val blocks: MutableSet<Vec2i>
)
