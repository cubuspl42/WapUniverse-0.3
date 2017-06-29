interface RezMetadataDao {
    fun findImageMetadata(fullyQualifiedImageSetId: String, i: Int): RezImageMetadata?
}
