package io.github.wapuniverse.view

import io.github.wapuniverse.common.util.Rectangle2Di

interface SnItem {
    var bbox: Rectangle2Di

    val selected: Boolean

    val hovered: Boolean
}
