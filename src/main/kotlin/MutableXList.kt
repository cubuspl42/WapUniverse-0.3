interface MutableXList<E> : XList<E>, MutableList<E> {
    companion object Factory {
        fun <E> create(mutableList: MutableList<E>): MutableXListImpl<E> =
                MutableXListImpl(mutableList)
    }
}