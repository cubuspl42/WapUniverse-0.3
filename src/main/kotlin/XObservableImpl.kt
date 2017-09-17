class XObservableImpl<out T> : XObservable<T> {

    private val listeners = mutableSetOf<XListener<T>>()

    override fun addListener(listener: XListener<T>) {
        listeners.add(listener)
    }

    override fun removeListener(listener: XListener<T>) {
        listeners.remove(listener)
    }
}