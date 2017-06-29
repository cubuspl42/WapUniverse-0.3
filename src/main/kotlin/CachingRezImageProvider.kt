class CachingRezImageProvider(
        private val rezMetadataDao: RezMetadataDao,
        private val rezImageLoader: RezImageLoader
) : RezImageProvider {
    private data class Key(val id: String, val i: Int)

    private val cache = hashMapOf<Key, RezImage>()

    suspend override fun provideImage(fullyQualifiedImageSetId: String, i: Int): RezImage? {
        val key = Key(fullyQualifiedImageSetId, i)
        return cache[key] ?: loadAndCache(key)
    }

    private suspend fun loadAndCache(key: Key): RezImage? =
            loadImage(key.id, key.i)?.let { rezImage ->
                cache[key] = rezImage
                rezImage
            }

    private suspend fun loadImage(fullyQualifiedImageSetId: String, i: Int): RezImage? {
        val imageMetadata = rezMetadataDao.findImageMetadata(fullyQualifiedImageSetId, i) ?: return null
        val image = rezImageLoader.loadImage(imageMetadata.rezPath) ?: return null
        return RezImage(image, imageMetadata.offset)
    }
}