package io.github.wapuniverse.controller

import io.github.wapuniverse.view.EventHandlingStatus
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_NOT_HANDLED
import javafx.scene.input.MouseEvent
import java.util.*


class SceneInputController {
    private val inputHandlers = HashSet<InputHandler>()

    fun onMousePressed(ev: MouseEvent) {
        dispatchMouseEvent(ev, InputHandler::onMousePressed)
    }

    fun onMouseReleased(ev: MouseEvent) {
        dispatchMouseEvent(ev, InputHandler::onMouseReleased)
    }

    fun onMouseMoved(ev: MouseEvent) {
        dispatchMouseEvent(ev, InputHandler::onMouseMoved)
    }

    fun onMouseDragged(ev: MouseEvent) {
        dispatchMouseEvent(ev, InputHandler::onMouseDragged)
    }

    private fun dispatchMouseEvent(ev: MouseEvent, onEvent: (InputHandler, MouseEvent) -> EventHandlingStatus) {
        inputHandlers
                .sortedBy { it.priority }
                .foldRight(EVENT_NOT_HANDLED, { ih, st ->
                    if (st == EVENT_HANDLED) return
                    else onEvent(ih, ev)
                })
    }

    fun addInputHandler(inputHandler: InputHandler) {
        inputHandlers.add(inputHandler)
    }

    fun removeInputHandler(inputHandler: InputHandler) {
        inputHandlers.remove(inputHandler)
    }
}
