interface MutableXCollection<E> : XCollection<E>, MutableCollection<E> {
    companion object Factory {
        fun <E> create(mutableCollection: MutableCollection<E>): MutableXCollectionImpl<E> =
                MutableXCollectionImpl(mutableCollection)
    }
}