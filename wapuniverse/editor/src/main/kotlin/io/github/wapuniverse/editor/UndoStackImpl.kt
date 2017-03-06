package io.github.wapuniverse.editor

import java.util.*

internal class UndoStackImpl : UndoStack {
    private val stack = Stack<UndoFrame>()

    private var head = 0

    override fun undo() {
        if (canUndo()) {
            val frame = stack[--head]
            frame.undo()
        }
    }

    override fun redo() {
        if (canRedo()) {
            val frame = stack[head++]
            frame.redo()
        }
    }

    fun push(frame: UndoFrame) {
        for (i in head..stack.size - 1) {
            stack.pop()
        }
        stack.push(frame)
        frame.redo()
        ++head
    }

    private fun canUndo() = head > 0

    private fun canRedo() = head < stack.size
}
