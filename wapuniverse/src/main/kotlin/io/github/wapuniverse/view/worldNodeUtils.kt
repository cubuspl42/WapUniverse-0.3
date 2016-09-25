package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import javafx.scene.transform.Affine

fun makeTransforms(camera: Vec2d, scale: Double): AffinePair {
    val transform = Affine()
    transform.appendTranslation(-camera.x, -camera.y)
    transform.appendScale(scale, scale)
    return AffinePair(transform)
}