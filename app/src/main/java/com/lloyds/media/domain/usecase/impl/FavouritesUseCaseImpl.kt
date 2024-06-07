package com.lloyds.media.domain.usecase.impl

import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.MovieDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.models.getOriginalName
import com.lloyds.media.domain.repository.FavouritesRepository
import com.lloyds.media.domain.usecase.FavouritesUseCase
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class FavouritesUseCaseImpl @Inject constructor(private val repository: FavouritesRepository) :
    FavouritesUseCase {
    override suspend fun addFavourites(
        mediaItem: MediaDetailsModel,
        createdAt: Date
    ): Work<MediaFavouritesEntity> {
        val work = withContext(Dispatchers.IO) {
            try {
                val entity = objectMapToFavouritesEntity(mediaItem, createdAt)
                val data: MediaFavouritesEntity = repository.addFavourites(entity)
                Work.result(data)
            } catch (e: Exception) {
                Work.backfire(e)
            }
        }
        return work
    }

    private fun objectMapToFavouritesEntity(
        mediaItem: MediaDetailsModel,
        createdAt: Date
    ): MediaFavouritesEntity {
        return MediaFavouritesEntity(
            id = mediaItem.id,
            name = mediaItem.getOriginalName(),
            mediaType = if (mediaItem is MovieDetailsModel) "movie" else "tv",
            overview = mediaItem.overview,
            imageUrl = mediaItem.posterPath,
            createdAt = createdAt
        )
    }

    override suspend fun isExistFavourites(mediaId: Int): Work<Boolean> {
        val work = withContext(Dispatchers.IO) {
            val optional = repository.isExistFavourites(mediaId)
            return@withContext Work.result(optional.isPresent)
        }
        return work
    }

    override suspend fun deleteFavourites(mediaId: Int): Work<Boolean> {
        val work = withContext(Dispatchers.IO) {
            val result = repository.deleteFavourites(mediaId)
            return@withContext Work.result(result)
        }
        return work
    }

    override suspend fun getAllFavourites(): Work<List<MediaFavouritesEntity>> {
        val work = withContext(Dispatchers.IO) {
            val data = repository.getAllFavourites()
            if (data.isEmpty()) {
                return@withContext Work.backfire(RuntimeException("There are currently no favourites available."))
            }
            return@withContext Work.result(repository.getAllFavourites())
        }
        return work
    }
}