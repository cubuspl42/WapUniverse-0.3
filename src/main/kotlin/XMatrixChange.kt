data class XMatrixChange<out E>(
        val addedElements: Set<XMatrixManipulatedElement<E>>,
        val changedElements: Set<XMatrixManipulatedElement<E>>,
        val removedElements: Set<XMatrixManipulatedElement<E>>
)
