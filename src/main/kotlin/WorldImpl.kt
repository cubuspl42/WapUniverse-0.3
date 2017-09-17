class WorldImpl(planes: MutableList<PlaneImpl>) : World {
    override val planes = MutableXList.create(planes)
}