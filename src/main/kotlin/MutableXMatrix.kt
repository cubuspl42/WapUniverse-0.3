interface MutableXMatrix<E> : XMatrix<E>, MutableMatrix<E> {
    companion object Factory {
        fun <E> create(mutableMatrix: MutableMatrix<E>): MutableXMatrixImpl<E> =
                MutableXMatrixImpl(mutableMatrix)
    }
}