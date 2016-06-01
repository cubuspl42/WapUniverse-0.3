package io.github.wapuniverse.controller

import io.github.wapuniverse.view.EventHandlingStatus
import javafx.scene.input.MouseEvent


interface InputHandler {
    fun onMousePressed(ev: MouseEvent): EventHandlingStatus
    fun onMouseReleased(ev: MouseEvent): EventHandlingStatus
    fun onMouseMoved(ev: MouseEvent): EventHandlingStatus
    fun onMouseDragged(ev: MouseEvent): EventHandlingStatus
    val priority: Int
}

open class InputHandlerImpl : InputHandler {
    override fun onMousePressed(ev: MouseEvent): EventHandlingStatus {
        return EventHandlingStatus.EVENT_NOT_HANDLED
    }

    override fun onMouseReleased(ev: MouseEvent): EventHandlingStatus {
        return EventHandlingStatus.EVENT_NOT_HANDLED
    }

    override fun onMouseMoved(ev: MouseEvent): EventHandlingStatus {
        return EventHandlingStatus.EVENT_NOT_HANDLED
    }

    override fun onMouseDragged(ev: MouseEvent): EventHandlingStatus {
        return EventHandlingStatus.EVENT_NOT_HANDLED
    }

    override val priority: Int = 0
}
