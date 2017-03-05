package io.github.wapuniverse.common.util

import javafx.scene.transform.Affine
import javafx.scene.transform.Transform

class Transform {
    val t: Transform = Affine()

    fun map(v: Vec2d): Vec2d {
        return Vec2d()
    }
}
