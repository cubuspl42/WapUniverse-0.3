class PlaneImpl(objects: MutableCollection<WObject>, tiles: MutableMatrix<Int>) : Plane {
    override val objects = MutableXCollection.create(objects)
    override val tiles = MutableXMatrix.create(tiles)
}