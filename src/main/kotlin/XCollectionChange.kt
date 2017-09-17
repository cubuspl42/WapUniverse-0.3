data class XCollectionChange<out E>(
        val added: Set<E>,
        val removed: Set<E>
)
