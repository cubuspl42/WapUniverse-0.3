interface XObservable<out T> {
    fun addListener(listener: XListener<T>)
    fun removeListener(listener: XListener<T>)
}