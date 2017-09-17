class MutableXListImpl<E>(private val mutableList: MutableList<E>) :
        MutableXList<E>,
        XObservable<XListChange<E>> by XObservableImpl<XListChange<E>>(),
        MutableList<E> by mutableList