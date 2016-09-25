package io.github.wapuniverse.presenter

import io.github.wapuniverse.core.AdaptiveEntity

class AdaptiveEntityPresenter(
        private val adaptiveEntity: AdaptiveEntity,
        private val worldPresenter: WorldPresenter
) : EntityPresenter {
    fun present() {
//        val imageSetDatabase = worldPresenter.imageSetDatabase
//        val levelIndex = worldPresenter.world.levelIndex
//        val image = worldPresenter.imageSetDatabase.findObjectImageMetadata(levelIndex, adaptiveEntity)
    }

    init {
        present()
    }
}
