package io.github.wapuniverse.view

import javafx.scene.transform.Affine

data class AffinePair(val transform: Affine) {
    val invTransform = transform.createInverse()
}
