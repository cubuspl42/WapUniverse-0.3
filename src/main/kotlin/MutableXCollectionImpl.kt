class MutableXCollectionImpl<E>(private val mutableCollection: MutableCollection<E>) :
        MutableXCollection<E>,
        XObservable<XCollectionChange<E>> by XObservableImpl<XCollectionChange<E>>(),
        MutableCollection<E> by mutableCollection