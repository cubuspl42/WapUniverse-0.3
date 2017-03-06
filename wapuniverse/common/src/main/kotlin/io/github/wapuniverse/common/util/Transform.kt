package io.github.wapuniverse.common.util

import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.transform.Affine

private fun makeFxTransform(translate: Vec2d, scale: Double): Affine {
    val affine = Affine()
    affine.appendScale(scale, scale)
    affine.appendTranslation(translate.x, translate.y)
    return affine
}

class Transform(translate: Vec2d = Vec2d(0.0, 0.0), scale: Double = 1.0) {
    private var affine: Affine = makeFxTransform(translate, scale)

    private constructor(affine: Affine) : this(Vec2d(), 0.0) {
        this.affine = affine
    }

    fun toAffine(): Affine {
        return affine.clone()
    }

    fun map(v: Vec2d): Vec2d {
        val p = affine.transform(Point2D(v.x, v.y))
        return Vec2d(p.x, p.y)
    }

    fun createInverse(): Transform {
        return Transform(affine.createInverse())
    }

    fun map(x: Double, y: Double): Vec2d {
        val p = affine.transform(x, y)
        return Vec2d(p.x, p.y)
    }

    fun map(r: Rect2d): Rect2d {
        val minV = map(r.minV)
        val maxV = map(r.maxV)
        val rm = Rect2d.fromBounds(minV, maxV)
        return rm
    }
}
