package com.lloyds.media.data.repository.datasource

import com.lloyds.media.infra.database.MediaDatabase
import com.lloyds.media.infra.database.dao.MediaFavouritesDao
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import java.util.Optional
import javax.inject.Inject

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
class FavouritesLocalDataSource @Inject constructor(private val mediaDatabase: MediaDatabase) {
    private val favouritesDao: MediaFavouritesDao by lazy { mediaDatabase.getMediaFavouritesDao() }

    suspend fun findById(id: Int): Optional<MediaFavouritesEntity> {
        return favouritesDao.findById(id)
    }

    suspend fun insert(entity: MediaFavouritesEntity): MediaFavouritesEntity {
        val optional = findById(entity.id)
        if (optional.isPresent) {
            throw RuntimeException("Media is already exist, Id : ${entity.id}")
        }
        favouritesDao.insert(entity)
        return favouritesDao.findById(entity.id).get()
    }

    suspend fun delete(id: Int): Boolean {
        favouritesDao.deleteById(id)
        return true
    }

    suspend fun getAllFavourites(): List<MediaFavouritesEntity> {
        return favouritesDao.findAll()
    }

}