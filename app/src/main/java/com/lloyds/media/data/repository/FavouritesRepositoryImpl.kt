package com.lloyds.media.data.repository

import com.lloyds.media.data.repository.datasource.FavouritesLocalDataSource
import com.lloyds.media.domain.repository.FavouritesRepository
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import java.util.Optional
import javax.inject.Inject

class FavouritesRepositoryImpl @Inject constructor(private val localDataSource: FavouritesLocalDataSource) :
    FavouritesRepository {
    override suspend fun addFavourites(favouritesEntity: MediaFavouritesEntity): MediaFavouritesEntity {
        return localDataSource.insert(favouritesEntity)
    }

    override suspend fun isExistFavourites(mediaId: Int): Optional<MediaFavouritesEntity> {
        return localDataSource.findById(mediaId)
    }

    override suspend fun deleteFavourites(mediaId: Int): Boolean {
        return localDataSource.delete(mediaId)
    }

    override suspend fun getAllFavourites(): List<MediaFavouritesEntity> {
        return localDataSource.getAllFavourites()
    }
}