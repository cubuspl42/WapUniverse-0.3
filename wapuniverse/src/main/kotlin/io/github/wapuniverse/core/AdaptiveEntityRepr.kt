package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Vec2i

data class AdaptiveEntityRepr(
        val scriptId: String,
        val position: Vec2i
) : EntityRepr
