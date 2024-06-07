package com.lloyds.media.domain.repository

import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import java.util.Optional

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface FavouritesRepository {
    suspend fun addFavourites(favouritesEntity: MediaFavouritesEntity): MediaFavouritesEntity
    suspend fun isExistFavourites(mediaId: Int): Optional<MediaFavouritesEntity>
    suspend fun deleteFavourites(mediaId: Int): Boolean
    suspend fun getAllFavourites(): List<MediaFavouritesEntity>
}