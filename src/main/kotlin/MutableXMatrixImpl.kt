class MutableXMatrixImpl<E>(private val mutableMatrix: MutableMatrix<E>) :
        MutableXMatrix<E>,
        XObservable<XMatrixChange<E>> by XObservableImpl<XMatrixChange<E>>(),
        MutableMatrix<E> by mutableMatrix